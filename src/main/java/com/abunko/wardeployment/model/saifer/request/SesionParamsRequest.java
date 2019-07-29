package com.abunko.wardeployment.model.saifer.request;

public class SesionParamsRequest {
    private String signatureType = "detached";
    private String embedSignatureTs = "false";
    private String embedDataTs = "false";
    private String embedCertificateType = "nothing";
    private String signatureTsVerifyOption = "ignore";
    private String dataTsVerifyOption  = "ignore";
    private String tsAdditionalVerifying = "false";
    private String dataToSignQualifier = "notSignedBefore";
    private String duplicateSign = "false";
    private String caId;
    private String cadesType = "CAdESXLong";
    private String nokkCompatible = "false";

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public String getEmbedSignatureTs() {
        return embedSignatureTs;
    }

    public void setEmbedSignatureTs(String embedSignatureTs) {
        this.embedSignatureTs = embedSignatureTs;
    }

    public String getEmbedDataTs() {
        return embedDataTs;
    }

    public void setEmbedDataTs(String embedDataTs) {
        this.embedDataTs = embedDataTs;
    }

    public String getEmbedCertificateType() {
        return embedCertificateType;
    }

    public void setEmbedCertificateType(String embedCertificateType) {
        this.embedCertificateType = embedCertificateType;
    }

    public String getSignatureTsVerifyOption() {
        return signatureTsVerifyOption;
    }

    public void setSignatureTsVerifyOption(String signatureTsVerifyOption) {
        this.signatureTsVerifyOption = signatureTsVerifyOption;
    }

    public String getDataTsVerifyOption() {
        return dataTsVerifyOption;
    }

    public void setDataTsVerifyOption(String dataTsVerifyOption) {
        this.dataTsVerifyOption = dataTsVerifyOption;
    }

    public String getTsAdditionalVerifying() {
        return tsAdditionalVerifying;
    }

    public void setTsAdditionalVerifying(String tsAdditionalVerifying) {
        this.tsAdditionalVerifying = tsAdditionalVerifying;
    }

    public String getDataToSignQualifier() {
        return dataToSignQualifier;
    }

    public void setDataToSignQualifier(String dataToSignQualifier) {
        this.dataToSignQualifier = dataToSignQualifier;
    }

    public String getDuplicateSign() {
        return duplicateSign;
    }

    public void setDuplicateSign(String duplicateSign) {
        this.duplicateSign = duplicateSign;
    }

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    public String getCadesType() {
        return cadesType;
    }

    public void setCadesType(String cadesType) {
        this.cadesType = cadesType;
    }

    public String getNokkCompatible() {
        return nokkCompatible;
    }

    public void setNokkCompatible(String nokkCompatible) {
        this.nokkCompatible = nokkCompatible;
    }
}
