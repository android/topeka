#!/bin/sh

here=`dirname $0`

dpis=("mdpi:144" "hdpi:216" "xhdpi:288" "xxhdpi:432" "xxxhdpi:640")

for dpi in "${dpis[@]}"; do
  key=${dpi%%:*}
  size=${dpi#*:}
  echo drawable-$key
  for svg in $here/svg/*.svg
  do
    png=$(echo $svg | sed 's/\.svg/\.png/' | awk -F/ '{print $NF;}')
    actualSize=$size
    if !( echo $svg | grep -q "icon.*" )
    then
      actualSize=$((2*$actualSize))
    fi
    inkscape $svg -w ${actualSize} -h ${actualSize} -e $here/../app/src/main/res/drawable-$key/$png
  done
done
