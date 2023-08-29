package h2.model;

import com.google.gson.annotations.SerializedName;

public class QrResp {

    /**
     * codeUrl
     */
    @SerializedName("code_url")
    private String codeUrl;

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }
}
