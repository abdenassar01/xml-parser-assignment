<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="Personnes">
        <html>
            <head>
                <title>Personnes</title>
            </head>
            <body>
                <ul>
                    <xsl:for-each select="Personne">
                        <li>
                            Sexe: <xsl:value-of select="@sexe" /><br/>  
                            Nom: <xsl:value-of select="nom" /><br/>
                            Prenom: <xsl:value-of select="prenom" /><br/>
                            E-mail: <xsl:value-of select="mail" /><br/>
                            Adresse: <xsl:value-of select="adresse" /><br/>
                            Telephone: <xsl:value-of select="telephone" /><br/>
                        </li>
                        <br/>
                    </xsl:for-each>
            </ul>

        </body>
        </html>
    </xsl:template>
</xsl:stylesheet>