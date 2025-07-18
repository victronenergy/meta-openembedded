SUMMARY = "ImageMagick is an image conversion toolkit"
SECTION = "console/utils"
HOMEPAGE = "https://www.imagemagick.org/"
DESCRIPTION = "ImageMagick is a collection of tools for displaying, converting, and \
editing raster and vector image files. It can read and write over 200 image file formats."
LICENSE = "ImageMagick"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2f9de66264141265b203cde9902819ea \
                    file://NOTICE;md5=bcbf1f1897b40ec8df39700cb560e9ed"
# FIXME: There are many more checked libraries. All should be added or explicitly disabled to get consistent results.
DEPENDS = "lcms bzip2 jpeg libpng tiff zlib fftw freetype libtool"

BASE_PV := "${PV}"
PV .= "-26"
SRC_URI = "git://github.com/ImageMagick/ImageMagick.git;branch=main;protocol=https"
SRCREV = "570a9a048bb0e3a5c221ca87be9408ae35f711e2"

S = "${WORKDIR}/git"

inherit autotools pkgconfig update-alternatives
export ac_cv_sys_file_offset_bits="64"

EXTRA_OECONF = "--program-prefix= --program-suffix=.im7 --without-perl --enable-largefile"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)} cxx webp xml"
PACKAGECONFIG[cxx] = "--with-magick-plus-plus,--without-magick-plus-plus"
PACKAGECONFIG[graphviz] = "--with-gvc,--without-gvc,graphviz"
PACKAGECONFIG[jp2] = "--with-jp2,,jasper"
PACKAGECONFIG[lzma] = "--with-lzma,--without-lzma,xz"
PACKAGECONFIG[openjpeg] = "--with-openjp2,--without-openjp2,openjpeg"
PACKAGECONFIG[pango] = "--with-pango,--without-pango,pango cairo"
PACKAGECONFIG[rsvg] = "--with-rsvg,--without-rsvg,librsvg"
PACKAGECONFIG[tcmalloc] = "--with-tcmalloc=yes,--with-tcmalloc=no,gperftools"
PACKAGECONFIG[webp] = "--with-webp,--without-webp,libwebp"
PACKAGECONFIG[wmf] = "--with-wmf,--without-wmf,libwmf"
PACKAGECONFIG[x11] = "--with-x,--without-x,virtual/libx11 libxext libxt"
PACKAGECONFIG[xml] = "--with-xml,--without-xml,libxml2"

do_install:append:class-target() {
    for file in MagickCore-config.im7 MagickWand-config.im7; do
        sed -i 's,${STAGING_DIR_TARGET},,g' "${D}${bindir}/$file"
    done

    if ${@bb.utils.contains('PACKAGECONFIG', 'cxx', 'true', 'false', d)}; then
        sed -i 's,${STAGING_DIR_TARGET},,g' "${D}${bindir}/Magick++-config.im7"
    fi

    if ${@bb.utils.contains('PACKAGECONFIG', 'xml', 'true', 'false', d)}; then
        xml_config="${D}${libdir}/ImageMagick-${BASE_PV}/config-Q16HDRI/configure.xml"
        sed -i 's,${S},,g' "$xml_config"
        sed -i 's,${B},,g' "$xml_config"
        sed -i 's,${RECIPE_SYSROOT},,g' "$xml_config"
    fi

    if ${@bb.utils.contains_any('PACKAGECONFIG', 'webp openjpeg', 'true', 'false', d)}; then
        sed -i 's,${HOSTTOOLS_DIR},${bindir},g' "${D}${sysconfdir}/ImageMagick-7/delegates.xml"
    fi
}

FILES:${PN} += "${libdir}/ImageMagick-${BASE_PV}/config-Q16* \
                ${datadir}/ImageMagick-7"

FILES:${PN}-dev += "${libdir}/ImageMagick-${BASE_PV}/modules-Q16/*/*.a"

FILES:${PN}-dbg += "${libdir}/ImageMagick-${BASE_PV}/modules-Q16/*/.debug/*"

BBCLASSEXTEND = "native nativesdk"

ALTERNATIVE_PRIORITY = "100"

ALTERNATIVE:${PN} = "animate compare composite conjure convert display \
    identify import magick-script mogrify montage stream"

ALTERNATIVE_TARGET[animate] = "${bindir}/animate.im7"
ALTERNATIVE_TARGET[compare] = "${bindir}/compare.im7"
ALTERNATIVE_TARGET[composite] = "${bindir}/composite.im7"
ALTERNATIVE_TARGET[conjure] = "${bindir}/conjure.im7"
ALTERNATIVE_TARGET[convert] = "${bindir}/convert.im7"
ALTERNATIVE_TARGET[display] = "${bindir}/display.im7"
ALTERNATIVE_TARGET[identify] = "${bindir}/identify.im7"
ALTERNATIVE_TARGET[import] = "${bindir}/import.im7"
ALTERNATIVE_TARGET[magick-script] = "${bindir}/magick-script.im7"
ALTERNATIVE_TARGET[mogrify] = "${bindir}/mogrify.im7"
ALTERNATIVE_TARGET[montage] = "${bindir}/montage.im7"
ALTERNATIVE_TARGET[stream] = "${bindir}/stream.im7"

ALTERNATIVE:${PN}-doc = "animate.1 compare.1 composite.1 conjure.1 \
    convert.1 display.1 identify.1 import.1 magick-script.1 mogrify.1 montage.1 stream.1"

ALTERNATIVE_LINK_NAME[animate.1] = "${mandir}/man1/animate.1"
ALTERNATIVE_TARGET[animate.1] = "${mandir}/man1/animate.im7.1"
ALTERNATIVE_LINK_NAME[compare.1] = "${mandir}/man1/compare.1"
ALTERNATIVE_TARGET[compare.1] = "${mandir}/man1/compare.im7.1"
ALTERNATIVE_LINK_NAME[composite.1] = "${mandir}/man1/composite.1"
ALTERNATIVE_TARGET[composite.1] = "${mandir}/man1/composite.im7.1"
ALTERNATIVE_LINK_NAME[conjure.1] = "${mandir}/man1/conjure.1"
ALTERNATIVE_TARGET[conjure.1] = "${mandir}/man1/conjure.im7.1"
ALTERNATIVE_LINK_NAME[convert.1] = "${mandir}/man1/convert.1"
ALTERNATIVE_TARGET[convert.1] = "${mandir}/man1/convert.im7.1"
ALTERNATIVE_LINK_NAME[display.1] = "${mandir}/man1/display.1"
ALTERNATIVE_TARGET[display.1] = "${mandir}/man1/display.im7.1"
ALTERNATIVE_LINK_NAME[identify.1] = "${mandir}/man1/identify.1"
ALTERNATIVE_TARGET[identify.1] = "${mandir}/man1/identify.im7.1"
ALTERNATIVE_LINK_NAME[import.1] = "${mandir}/man1/import.1"
ALTERNATIVE_TARGET[import.1] = "${mandir}/man1/import.im7.1"
ALTERNATIVE_LINK_NAME[magick-script.1] = "${mandir}/man1/magick-script.1"
ALTERNATIVE_TARGET[magick-script.1] = "${mandir}/man1/magick-script.im7.1"
ALTERNATIVE_LINK_NAME[mogrify.1] = "${mandir}/man1/mogrify.1"
ALTERNATIVE_TARGET[mogrify.1] = "${mandir}/man1/mogrify.im7.1"
ALTERNATIVE_LINK_NAME[montage.1] = "${mandir}/man1/montage.1"
ALTERNATIVE_TARGET[montage.1] = "${mandir}/man1/montage.im7.1"
ALTERNATIVE_LINK_NAME[stream.1] = "${mandir}/man1/stream.1"
ALTERNATIVE_TARGET[stream.1] = "${mandir}/man1/stream.im7.1"

CVE_STATUS[CVE-2007-1667] = "cpe-incorrect: CVE should not include a CPE for imagemagick"
CVE_STATUS[CVE-2014-9804] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9805] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9806] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9807] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9808] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9809] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9810] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9811] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9812] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9813] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9814] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9815] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9816] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9817] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9818] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9819] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9820] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9821] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9822] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9823] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9824] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9825] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9826] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9827] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9828] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9829] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9830] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9831] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9848] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9852] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9853] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9854] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2014-9907] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-10062] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.1-10"
CVE_STATUS[CVE-2016-10144] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.7-1"
CVE_STATUS[CVE-2016-10145] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.7-1"
CVE_STATUS[CVE-2016-10146] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.6-8"
CVE_STATUS[CVE-2016-5118] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.1-7"
CVE_STATUS[CVE-2016-7513] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7514] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.1-0"
CVE_STATUS[CVE-2016-7515] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7516] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7517] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7518] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7519] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7520] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7521] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7522] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7523] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7524] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7525] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7526] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7527] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7528] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7529] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7530] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7531] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.1-0"
CVE_STATUS[CVE-2016-7532] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7533] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7534] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7535] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7536] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7537] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2016-7538] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 6.9.4-0"
CVE_STATUS[CVE-2017-5506] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.4-4"
CVE_STATUS[CVE-2017-5509] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.4-4"
CVE_STATUS[CVE-2017-5510] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.4-4"
CVE_STATUS[CVE-2017-5511] = "cpe-incorrect: The current version (7.1.1) is not affected by the CVE which affects versions at least earlier than 7.0.4-3"
