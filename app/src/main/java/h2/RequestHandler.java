package h2;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.google.gson.JsonSyntaxException;
import com.hjq.gson.factory.GsonFactory;
import com.hjq.http.EasyLog;
import com.hjq.http.config.IRequestHandler;
import com.hjq.http.exception.CancelException;
import com.hjq.http.exception.HttpException;
import com.hjq.http.request.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.netty.handler.codec.http.multipart.HttpData;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/12/07
 * desc   : 请求处理类
 */
public final class RequestHandler implements IRequestHandler {

    private final Application mApplication;

    public RequestHandler(Application application) {
        mApplication = application;
    }

    @Override
    public Object requestSuccess(@NonNull HttpRequest<?> httpRequest, @NonNull Response response, @NonNull Type type) throws Exception {

        if (Response.class.equals(type)) {
            return response;
        }

        if (!response.isSuccessful()) {
            // 返回响应异常
//            throw new ResponseException(mApplication.getString(R.string.http_response_error) + "，responseCode：" + response.code() + "，message：" + response.message(), response);
        }

        if (Headers.class.equals(type)) {
            return response.headers();
        }

        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        if (InputStream.class.equals(type)) {
            return body.byteStream();
        }

        String text = null;
        try {
            text = body.string();
        } catch (IOException e) {
            // 返回结果读取异常
//            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
        }

        // 打印这个 Json 或者文本
        EasyLog.printLog(httpRequest,text);

        if (String.class.equals(type)) {
            return text;
        }

        if (JSONObject.class.equals(type)) {
            try {
                // 如果这是一个 JSONObject 对象
                return new JSONObject(text);
            } catch (JSONException e) {
//                throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
            }
        }

        if (JSONArray.class.equals(type)) {
            try {
                // 如果这是一个 JSONArray 对象
                return new JSONArray(text);
            } catch (JSONException e) {
//                throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
            }
        }

        Object result = null;

        try {
            result = GsonFactory.getSingletonGson().fromJson(text, type);
        } catch (JsonSyntaxException e) {
            // 返回结果读取异常
//            throw new DataException(mApplication.getString(R.string.http_data_explain_error), e);
        }

        if (result instanceof HttpData) {
            HttpData model = (HttpData) result;

          /*  if (model.isRequestSucceed()) {
                // 代表执行成功
                return result;
            }

            if (model.isTokenFailure()) {
                // 代表登录失效，需要重新登录
                throw new TokenException(mApplication.getString(R.string.http_token_error));
            }

            // 代表执行失败
            throw new ResultException(model.getMessage(), model);*/
        }
        return result;
    }

    @Override
    public Exception requestFail(@NonNull HttpRequest<?> httpRequest, @NonNull Exception e) {

        // 判断这个异常是不是自己抛的
        if (e instanceof HttpException) {

            return e;
        }

        if (e instanceof SocketTimeoutException) {
//            return new TimeoutException(mApplication.getString(R.string.http_server_out_time), e);
        }

        if (e instanceof UnknownHostException) {
            NetworkInfo info = ((ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            // 判断网络是否连接
            if (info == null || !info.isConnected()) {
                // 没有连接就是网络异常
//                return new NetworkException(mApplication.getString(R.string.http_network_error), e);
            }

            // 有连接就是服务器的问题
//            return new ServerException(mApplication.getString(R.string.http_server_error), e);
        }

        if (e instanceof IOException) {
            //e = new CancelException(context.getString(R.string.http_request_cancel), e);
            return new CancelException("", e);
        }

        return new HttpException(e.getMessage(), e);
    }


}