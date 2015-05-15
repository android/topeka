#!/bin/bash

sources=("https://raw.githubusercontent.com/Polymer/topeka-elements/master/avatars.html"
    "https://raw.githubusercontent.com/Polymer/topeka-elements/master/category-icons.html"
    "https://raw.githubusercontent.com/Polymer/topeka-elements/master/category-images.html")

#mkdir raw
cd raw

#for source in ${sources[@]};
#do
#  wget $source raw
#done

for file in `ls`;
do
  echo $file
  image_type=`echo $file | cut -d"." -f1`
  image_size=`cat $file | grep "iconSize" | cut -d"\"" -f 4`
  echo $image_type
  echo $image_size

  for image in $(cat $file | grep "<g")
  do
#    image_name=`echo $image | cut -d"\"" -f2`
    echo $image_type $image
  done
done

cd ..

#rm -r raw
