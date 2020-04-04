#!/bin/bash

# This script calculates the percentage of bash related content in
# the dummy blog engine running on localhost:8080.
#
# Usage:
# - Start blog engine:
#     mvn spring-boot:run
# - Run script:
#     ./calculate-bash-stats.sh

# strict mode
# http://redsymbol.net/articles/unofficial-bash-strict-mode/
set -euo pipefail

main()
{
    get_posts 0 | parse_response | only_published | add_length | tee /dev/tty \
                | add_isBash | aggregate | report
}

get_posts() {
    local page_num=$1
    local response=$(curl --silent --show-error "http://localhost:8080/posts?pageNumber=$page_num")

    echo $response

    local last_page_num=$(echo $response | jq --compact-output '.lastPageNumber')
    if [ "$page_num" != "$last_page_num" ]; then
        local next_page=$(($page_num + 1))
        get_posts $next_page
    fi
}

parse_response()
{
    jq --compact-output '.posts[] | {id: .id, status: .status, tags: .tags}'
}

only_published()
{
    jq --compact-output '. | select( .status=="published" )
                           | del( .status )'
}

add_length()
{
    xargs -d'\n' -I '{}' bash -c "add_length_ '{}' ."
}

add_length_()
{
    local post=$1
    local id=$(echo $post | jq '.id')
    local length=$(curl --silent --show-error "http://localhost:8080/posts/$id" \
                  | jq --raw-output '.content' | wc --chars)
    echo $post | jq --compact-output '. + {length: '$length'} | del( .id )'
}
export -f add_length_

add_isBash()
{
    jq --compact-output '. + {isBash: (. | any(.tags[] ; contains("Bash")))}
             | del( .tags )'
}

aggregate()
{
	jq --slurp --compact-output 'group_by(.["isBash"])[]
                   | map(.length) as $carry | .[0]
	           | . + {lengthTotal: $carry | add}
		   | del(.length)' \
        | jq --slurp --compact-output '.'
}

report()
{
    while read object
    do
        local non_bash_length=$(echo $object | jq -c '.[] | select( .isBash==false ) | .lengthTotal')
        local bash_length=$(echo $object | jq -c '.[] | select( .isBash==true ) | .lengthTotal')
        local total=$(echo "$non_bash_length + $bash_length" | bc)
        local ratio=$(echo "scale=4;($bash_length / $total) * 100" | bc)

        echo "Ratio: $ratio %"
    done < "${1:-/dev/stdin}"
}
main

