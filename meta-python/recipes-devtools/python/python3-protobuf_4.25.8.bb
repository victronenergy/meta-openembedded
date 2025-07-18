DESCRIPTION = "Protocol Buffers"
HOMEPAGE = "https://developers.google.com/protocol-buffers/"
SECTION = "devel/python"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://PKG-INFO;beginline=8;endline=8;md5=53dbfa56f61b90215a9f8f0d527c043d"

inherit pypi setuptools3
SRC_URI[sha256sum] = "6135cf8affe1fc6f76cced2641e4ea8d3e59518d1f24ae41ba97bcad82d397cd"

# http://errors.yoctoproject.org/Errors/Details/184715/
# Can't find required file: ../src/google/protobuf/descriptor.proto
CLEANBROKEN = "1"

UPSTREAM_CHECK_REGEX = "protobuf/(?P<pver>\d+(\.\d+)+)/"

DEPENDS += "protobuf"

RDEPENDS:${PN} += " \
    python3-datetime \
    python3-json \
    python3-logging \
    python3-netclient \
    python3-numbers \
    python3-pkgutil \
    python3-unittest \
"

# For usage in other recipies when compiling protobuf files (e.g. by grpcio-tools)
BBCLASSEXTEND = "native nativesdk"

DISTUTILS_BUILD_ARGS += "--cpp_implementation"
DISTUTILS_INSTALL_ARGS += "--cpp_implementation"

do_compile:prepend:class-native () {
    export KOKORO_BUILD_NUMBER="1"
}

do_install:append () {
    # Remove useless and problematic .pth file. python3-protobuf is installed in the standard
    # location of site packages. No need for such .pth file.
    # NOTE: do not drop this removal until the following issue in upstream cpython is resolved:
    # https://github.com/python/cpython/issues/122220
    rm -f ${D}${PYTHON_SITEPACKAGES_DIR}/protobuf-*-nspkg.pth
}
