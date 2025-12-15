package com.d1vivek.jksgeneratorplugin

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Security
import java.security.cert.X509Certificate
import java.util.*

object JksGenerator {

    /**
     * Generate a JKS keystore with a self-signed certificate
     */
    fun generateJks(
        keystoreFile: String,
        keystorePassword: String,
        alias: String,
        keyPassword: String,
        countryCode: String,
        cName: String = "",
        orgUnit: String = "",
        org: String = "",
        localityCity: String = "",
        stateProvince: String = "",
        validity: Int = 100,
    ): Boolean {
        try {
            if (Security.getProvider("BC") == null) {
                Security.addProvider(BouncyCastleProvider())
            }

            val finalCommonName = cName.ifBlank { "viveksharma.com.np" }


            // 1. Generate RSA KeyPair
            val keyPairGen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGen.initialize(2048)
            val keyPair: KeyPair = keyPairGen.generateKeyPair()

            // 2. Create X500Name for subject & issuer (self-signed)
            val subject =
                X500Name("CN=$finalCommonName, OU=$orgUnit, O=$org, L=$localityCity, ST=$stateProvince, C=$countryCode")

            // 3. Build certificate
            val serialNumber = BigInteger.valueOf(System.currentTimeMillis())
            val notBefore = Date()
            val notAfter = Date(System.currentTimeMillis() + validity * 365 * 24L * 60 * 60 * 1000)

            val san = GeneralNames(
                GeneralName(
                    GeneralName.dNSName,
                    finalCommonName
//                            GeneralName.uniformResourceIdentifier,
//                    "android://com.example.myapp"
                )
            )


            val certBuilder: X509v3CertificateBuilder = JcaX509v3CertificateBuilder(
                subject,
                serialNumber,
                notBefore,
                notAfter,
                subject,
                keyPair.public
            ).addExtension(
                Extension.subjectAlternativeName,
                false,
                san
            )

            val signer = JcaContentSignerBuilder("SHA256withRSA").build(keyPair.private)
            val certificate: X509Certificate = JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certBuilder.build(signer))

            // 4. Create JKS keystore
            val keyStore = KeyStore.getInstance("JKS")
            keyStore.load(null, keystorePassword.toCharArray())
            keyStore.setKeyEntry(alias, keyPair.private, keyPassword.toCharArray(), arrayOf(certificate))

            // 5. Write keystore to file
            val destFile = File(keystoreFile, "d1_keystore.jks")
            FileOutputStream(destFile).use { fos ->
                keyStore.store(fos, keystorePassword.toCharArray())
            }

            println("JKS keystore generated at $destFile with alias '$alias'")

//            // check
//            println("Hidden: ${destFile.isHidden} >>>  ${destFile.exists()}")
//
//            val ks = KeyStore.getInstance("JKS")
//            FileInputStream(destFile).use {
//                ks.load(it, keystorePassword.toCharArray())
//            }
//
//            val cert = ks.getCertificate(alias) as X509Certificate
//            val sanList = cert.subjectAlternativeNames
//
//            println(sanList)


            return true
        } catch (e: Exception) {
            println("JKS error >>> ${e.message}")
            return false
        }
    }
}
