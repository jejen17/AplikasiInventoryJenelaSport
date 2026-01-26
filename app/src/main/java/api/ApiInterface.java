package api;

import java.util.List;

import model.HistoryResponse;
import model.LoginResponse;
import model.ProdukItem;
import model.SizeItem;
import model.SpkResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("android/barang-masuk")
    Call<ResponseBody> inputBarangMasuk(
            @Header("Authorization") String token,
            @Field("id_produk") String idProduk,
            @Field("id_size") String idSize,
            @Field("jumlah") int jumlah,
            @Field("tanggal") String tanggal
    );

    @GET("android/list-produk")
    Call<List<ProdukItem>> getListProduk(
            @Header("Authorization") String token
    );


    @GET("android/list-size/{idProduk}")
    Call<List<SizeItem>> getListSize(
            @Header("Authorization") String token,
            @Path("idProduk") String idProduk
    );

    @GET("android/riwayat")
    Call<HistoryResponse> getRiwayat(
            @Header("Authorization") String token
    );

}
