/*
 * Created by Mehmet Ozdemir on 9/22/20 9:53 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/22/20 9:53 AM
 */

package com.likapalab.locapp.models.interfaces;

import com.likapalab.locapp.models.request.DirectionRequest;
import com.likapalab.locapp.models.response.DirectionResponse;
import com.likapalab.locapp.services.ApiService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IHuaweiMapServices {

    @POST(ApiService.ROUTE_SERVICE + "{type}")
    Call<DirectionResponse> getDirectionApiRequest(@Path("type") String type, @Query("key") String apiKey, @Body DirectionRequest directionRequest);

}
