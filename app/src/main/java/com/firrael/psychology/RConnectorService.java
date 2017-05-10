package com.firrael.psychology;

import com.firrael.psychology.model.Result;
import com.firrael.psychology.model.StatisticsResult;
import com.firrael.psychology.model.UserResult;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by firrael on 25.05.2016.
 */
public interface RConnectorService {
    //String API_ENDPOINT = "http://127.0.0.1:3000";
    String API_ENDPOINT = "http://10.0.3.2:3000";
    //String API_ENDPOINT = "https://firrael.herokuapp.com";

    @FormUrlEncoded
    @POST("/user/login")
    Observable<UserResult> login(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("/user/startup_login")
    Observable<UserResult> startupLogin(@Field("login") String login, @Field("token") String token);

    @FormUrlEncoded
    @POST("/user")
    Observable<UserResult> createAccount(@Field("login") String login, @Field("password") String password, @Field("email") String email, @Field("age") int age, @Field("time") int time);

    @FormUrlEncoded
    @POST("/user/fcm_token")
    Observable<Result> sendFCMToken(@Field("user_id") long userId, @Field("fcm_token") String fcmToken);

    @FormUrlEncoded
    @POST("/user/update")
    Observable<UserResult> updateInfo(@Field("user_id") long userId, @Field("email") String email, @Field("age") int age, @Field("time") int time);

    @FormUrlEncoded
    @POST("/user/results_reaction")
    Observable<Result> sendReactionResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times);

    @FormUrlEncoded
    @POST("/user/results_stress")
    Observable<Result> sendStressResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("misses") long misses);

    @FormUrlEncoded
    @POST("/user/results_focusing")
    Observable<Result> sendFocusingResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("error_values[]") ArrayList<Long> errors);

    @FormUrlEncoded
    @POST("/user/results_stability")
    Observable<Result> sendAttentionStabilityResults(@Field("user_id") long userId, @Field("times[]") ArrayList<Double> times, @Field("misses") long misses, @Field("errors_value") long errors);

    @FormUrlEncoded
    @POST("/user/results_complex")
    Observable<Result> sendComplexMotorReactionResults(@Field("user_id") long userId, @Field("wins") long wins, @Field("fails") long fails, @Field("misses") long misses);

    @FormUrlEncoded
    @POST("/user/results_volume")
    Observable<Result> sendAttentionVolumeResults(@Field("user_id") long userId, @Field("wins") long wins, @Field("fails") long fails, @Field("misses") long misses);

    @FormUrlEncoded
    @POST("/user/statistics")
    Observable<StatisticsResult> fetchStatistics(@Field("user_id") long userId);

/*
    @POST("/user/load_user_photo")
    Observable<ImageResult> loadImage();

    @FormUrlEncoded
    @POST("/user/save_user_photo")
    Observable<ImageResult> saveImage(@Field("image") Bitmap imageBitmap);

    @FormUrlEncoded
    @POST("/group")
    Observable<CreateGroupResult> createGroup(@Field("title") String groupName, @Field("user_id") long creatorId);

    @FormUrlEncoded
    @POST("/group/fetch")
    Observable<GroupFetchResult> getGroups(@Field("user_id") long creatorId);

    @FormUrlEncoded
    @POST("/group/send_message")
    Observable<SendMessageResult> sendMessage(@Field("group_id") long groupId, @Field("user_id") long userId, @Field("text") String message);

    @FormUrlEncoded
    @POST("/group/fetch_messages")
    Observable<List<Message>> fetchMessages(@Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group/add_invite_user")
    Observable<AddInviteUserResult> addInviteUser(@Field("user_login_or_email") String loginOrEmail, @Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group/add_user")
    Observable<AddUserResult> addUser(@Field("user_id") long userId, @Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group/remove_user")
    Observable<RemoveUserResult> removeUser(@Field("login") String removeLogin, @Field("group_id") long groupId);

    @FormUrlEncoded
    @POST("/group/fetch_users")
    Observable<List<ChatUser>> fetchUsers(@Field("group_id") long groupId);


    // TODO for testing
    @FormUrlEncoded
    @POST("/user/send_pns_to_everyone")
    Observable<SendPNResult> sendPNToEveryone(@Field("user_id") long userId, @Field("title") String title, @Field("text") String text);

    @FormUrlEncoded
    @POST("/user/send_pns_to_group")
    Observable<SendPNResult> sendPNToGroup(@Field("group_id") long groupId, @Field("title") String title, @Field("text") String text);

    @FormUrlEncoded
    @POST("/group/start_call")
    Observable<StartCallResult> startCall(@Field("user_id") long userId);

    @FormUrlEncoded
    @POST("/group/invite_to_call")
    Observable<InviteToCallResult> inviteToCall(@Field("user_id") long userId, @Field("socket_address") String socketAddress, @Field("call_id") String callId);*/
}