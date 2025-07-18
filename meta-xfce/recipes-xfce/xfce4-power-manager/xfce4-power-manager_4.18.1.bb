SUMMARY = "Power manager for the Xfce desktop environment"
HOMEPAGE = "https://docs.xfce.org/xfce/xfce4-power-manager/start"
SECTION = "x11"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

inherit xfce features_check

REQUIRED_DISTRO_FEATURES = "x11"

DEPENDS += "libnotify libxrandr virtual/libx11 libxext xfce4-panel upower libxscrnsaver"

SRC_URI[sha256sum] = "6b08b10c4cb7516377dbd32c6cc2296a9faf47022c555f11e75b38fde14aff03"

EXTRA_OECONF = " \
    --enable-network-manager \
    --enable-panel-plugins \
"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'polkit', d)}"
PACKAGECONFIG[polkit] = "--enable-polkit, --disable-polkit, polkit"

PACKAGES += "xfce4-powermanager-plugin"

FILES:${PN} += " \
    ${datadir}/polkit-1 \
    ${datadir}/metainfo \
"

FILES:xfce4-powermanager-plugin = " \
    ${libdir}/xfce4 \
    ${datadir}/xfce4 \
"

RDEPENDS:xfce4-powermanager-plugin = "${PN}"
RDEPENDS:${PN} = "networkmanager ${@bb.utils.contains('DISTRO_FEATURES','systemd','','consolekit',d)}"

# xfce4-brightness-plugin was replaced by xfce4-powermanager-plugin
RPROVIDES:xfce4-powermanager-plugin += "xfce4-brightness-plugin"
RREPLACES:xfce4-powermanager-plugin += "xfce4-brightness-plugin"
RCONFLICTS:xfce4-powermanager-plugin += "xfce4-brightness-plugin"
