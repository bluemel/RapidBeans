<?xml version="1.0" encoding="UTF-8"?>

<!--
    XSL test stylesheet
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:import href="string.xsl"/>

<xsl:output method="text" version="1.0" encoding="iso-8859-1" indent="yes"/>

<xsl:param name="testp1"/>
<xsl:param name="testp2"/>

<xsl:variable name="newline">
	<xsl:text>
</xsl:text>
</xsl:variable>

<xsl:template match="//root">
	<xsl:text>Hello folks!</xsl:text>
	<xsl:value-of select="$newline" />

	<xsl:value-of select="$newline" />
	<xsl:text>// BEGIN manual code section
// sectionOne
</xsl:text>
	<xsl:text>// END manual code section
// sectionOne
</xsl:text>
	<xsl:value-of select="$newline" />

	<xsl:call-template name="String.upperFirstCharacter">
	  <xsl:with-param name="string" select="'upper'" />
	</xsl:call-template>
	<xsl:value-of select="$newline" />

	<xsl:if test="$testp1">
		<xsl:text>testp1 = "</xsl:text>
		<xsl:value-of select="$testp1" />
		<xsl:text>"</xsl:text>
		<xsl:value-of select="$newline" />
	</xsl:if>

	<xsl:text>// BEGIN manual code section
// sectionTwo
</xsl:text>
	<xsl:text>// END manual code section
// sectionTwo
</xsl:text>

	<xsl:if test="$testp2">
		<xsl:text>testp2 = "</xsl:text>
		<xsl:value-of select="$testp2" />
		<xsl:text>"</xsl:text>
		<xsl:value-of select="$newline" />
	</xsl:if>

	<xsl:for-each select="testelement">
		<xsl:text>testelement: </xsl:text>
		<xsl:value-of select="@name" />
		<xsl:text>
</xsl:text>
	</xsl:for-each>
</xsl:template>

</xsl:stylesheet>
