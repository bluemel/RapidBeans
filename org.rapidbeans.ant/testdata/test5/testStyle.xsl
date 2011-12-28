<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="iso-8859-1" indent="yes"/>
<xsl:param name="package"/>
<xsl:template match="//beantype">
<xsl:text>/*
 * EasyBiz Framework: </xsl:text><xsl:value-of select="@name"/><xsl:text>.java
 *
 * Copyright Martin Bluemel, 2005
 *
 * Oct 29, 2005
 */

package </xsl:text><xsl:value-of select="$package"/><xsl:text>;

/**
 * a BizBean.
 *
 * @author Easybiz Code Generation
 */
public final class </xsl:text><xsl:value-of select="@name"/><xsl:text> {

    // TEST this section should be preserved
    // BEGIN manual code section
    // </xsl:text><xsl:value-of select="@name"/><xsl:text>.class
    // END manual code section

    /**
     * constructor.
     */
    public </xsl:text><xsl:value-of select="@name"/><xsl:text>() {
    }
}</xsl:text>
</xsl:template>
</xsl:stylesheet>
