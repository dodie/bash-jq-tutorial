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
    PAGE_NUM=$1
    RESPONSE=$(curl --silent --show-error "http://localhost:8080/posts?pageNumber=$PAGE_NUM")

    echo $RESPONSE

    LAST_PAGE_NUM=$(echo $RESPONSE | jq --compact-output '.lastPageNumber')
    if [ "$PAGE_NUM" != "$LAST_PAGE_NUM" ]; then
        NEXT_PAGE=$(($PAGE_NUM + 1))
        get_posts $NEXT_PAGE
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
    POST=$1
    ID=$(echo $POST | jq '.id')
    LENGTH=$(curl --silent --show-error "http://localhost:8080/posts/$ID" \
                  | jq --raw-output '.content' | wc --chars)
    echo $POST | jq --compact-output '. + {length: '$LENGTH'} | del( .id )'
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
    while read OBJECT
    do
        NON_BASH_LENGTH=$(echo $OBJECT | jq -c '.[] | select( .isBash==false ) | .lengthTotal')
        BASH_LENGTH=$(echo $OBJECT | jq -c '.[] | select( .isBash==true ) | .lengthTotal')
        TOTAL=$(echo "$NON_BASH_LENGTH + $BASH_LENGTH" | bc)
        RATIO=$(echo "scale=4;($BASH_LENGTH / $TOTAL) * 100" | bc)

        echo "Ratio: $RATIO %"
    done < "${1:-/dev/stdin}"
}
main

