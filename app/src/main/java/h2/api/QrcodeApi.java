package h2.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

public class QrcodeApi implements IRequestApi {
    @NonNull
    @Override
    public String getApi() {
        return "agentapi/get_device_qrcode";
    }

    private String sn = "";

    public QrcodeApi setSn(String s) {
        sn = s;
        return this;
    }
}
