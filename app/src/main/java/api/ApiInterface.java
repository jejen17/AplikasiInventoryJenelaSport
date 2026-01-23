package api;

import model.LoginResponse;
import model.SpkResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("karyawan/login")
    Call<LoginResponse> loginUser(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("android/spk-aktif")
    Call<SpkResponse> getSpkList(
            @Header("Authorization") String token
    );

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("android/update-progress")
    Call<ResponseBody> storeProgress(
          @Header("Authorization") String token,
          @Field("id_detail") String idDetail,
          @Field("jumlah") int jumlah
    );
}
