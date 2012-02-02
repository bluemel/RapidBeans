<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--
<xsl:import href="Java.xsl"/>
-->

<xsl:output method="text" version="1.0" encoding="iso-8859-1" indent="yes"/>

<!--
<xsl:param name="in"/>
-->

<xsl:template match="//units/unit">
	<xsl:if test="">
		<xsl:message>unit: <xsl:value-of select="@id"/></xsl:message>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>
