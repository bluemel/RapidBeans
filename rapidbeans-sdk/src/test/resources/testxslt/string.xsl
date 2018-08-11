<?xml version="1.0" encoding="iso-8859-1"?>
<!--
  XSLT test include
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template name="String.upperFirstCharacter">
	<xsl:param name="string"/>
	<xsl:value-of select="translate(substring($string, 1, 1), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
	<xsl:value-of select="substring($string, 2, string-length($string) - 1)"/>
</xsl:template>

</xsl:stylesheet>
