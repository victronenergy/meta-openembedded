SUMMARY = "Free and Open On-Chip Debugging, In-System Programming and Boundary-Scan Testing"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
DEPENDS = "libusb-compat libftdi"
RDEPENDS_${PN} = "libusb1"

SRC_URI = " \
    git://git.code.sf.net/p/openocd/code;protocol=git;name=openocd \
    git://git.savannah.gnu.org/git2cl.git;protocol=git;destsuffix=tools/git2cl;name=git2cl \
    git://github.com/msteveb/jimtcl.git;protocol=git;destsuffix=git/jimtcl;name=jimtcl \
    git://gitlab.zapb.de/libjaylink/libjaylink.git;protocol=https;destsuffix=git/src/jtag/drivers/libjaylink;name=libjaylink \
    file://0001-Fix-libusb-1.0.22-deprecated-libusb_set_debug-with-l.patch \
"

SRCREV_FORMAT = "openocd"
SRCREV_openocd = "cdf1e826eb23c29de1019ce64125f644f01b0afe"
SRCREV_git2cl = "8373c9f74993e218a08819cbcdbab3f3564bbeba"
SRCREV_jimtcl = "a9bf5975fd0f89974d689a2d9ebd0873c8d64787"
SRCREV_libjaylink = "8645845c1abebd004e991ba9a7f808f4fd0c608b"

PV = "0.10+gitr${SRCPV}"
S = "${WORKDIR}/git"

inherit pkgconfig autotools-brokensep gettext

BBCLASSEXTEND += "nativesdk"

EXTRA_OECONF = "--enable-ftdi --disable-doxygen-html"

do_configure() {
    ./bootstrap nosubmodule
    oe_runconf ${EXTRA_OECONF}
}

do_install() {
    oe_runmake DESTDIR=${D} install
    if [ -e "${D}${infodir}" ]; then
      rm -Rf ${D}${infodir}
    fi
    if [ -e "${D}${mandir}" ]; then
      rm -Rf ${D}${mandir}
    fi
    if [ -e "${D}${bindir}/.debug" ]; then
      rm -Rf ${D}${bindir}/.debug
    fi
}

FILES_${PN} = " \
  ${datadir}/openocd/* \
  ${bindir}/openocd \
  "

PACKAGECONFIG[sysfsgpio] = "--enable-sysfsgpio,--disable-sysfsgpio"
PACKAGECONFIG ??= "sysfsgpio"
