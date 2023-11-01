#/usr/bin/env bash

for package in ${1// / }
do
    package_name=${package%.tar.gz}
    tar -xvf $package
    cd ./"${package_name}"
    aclocal
    autoheader
    automake --gnu --add-missing --copy --foreign
    autoconf -f -Wall
    ./configure
    make
    checkinstall
    dpkg -i "${package_name}".deb
    cd ./
    rm -rf ./"${package_name}"
    rm -rf ./"${package}"
done
