/*
 * Created by Mehmet Ozdemir on 8/31/20 10:06 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:00 AM
 */

package com.likapalab.locapp.utils;

public class Constants {

    public static final String AGC_API_KEY = "CgB6e3x9nBWtr7ZpJIf2Td+h4FZCLGVt4LvyEOngWIBKNRmALt169iphKZVIOldjM/8hTL0WRvLfEiTx3GjA0tZE";

    public static final String BUNDLE_ARG_KEY_MAP_VIEW_API_KEY = "bundle_arg_key_map_view_api_key";

    public static final int ACTIVITY_REQUEST_CODE_HUAWEI_SIGN_IN = 53;

    public static final int NOTIFICATION_ID_USER_NOTIFY = 53;
    public static final int NOTIFICATION_ID_GEOFENCE_NOTIFY = 5353;

    public static final String NOTIFICATION_CHANNEL_ID_USER_NOTIFY = "notification_channel_user_notify";
    public static final String NOTIFICATION_CHANNEL_ID_GEOFENCE_NOTIFY = "notification_channel_geofence_notify";

    public static final String NOTIFICATION_CHANNEL_NAME_USER_NOTIFY = "User Notifications";
    public static final String NOTIFICATION_CHANNEL_NAME_GEOFENCE_NOTIFY = "Geofence Notifications";

    public static final String INTENT_ACTION_PUSH_TOKEN_RECEIVED = "intent_action_push_token_received";
    public static final String INTENT_ACTION_VENUE_INFO_VIEW_PAGER_HEIGHT_RECEIVED = "intent_action_venue_info_view_pager_height_received";
    public static final String INTENT_ACTION_INSTRUCTION_INFO_VIEW_PAGER_HEIGHT_RECEIVED = "intent_action_instruction_info_view_pager_height_received";
    public static final String INTENT_ACTION_VENUE_LIST_RECEIVED = "intent_action_venue_list_received";
    public static final String INTENT_ACTION_ROUTE_RECEIVED = "intent_action_route_received";

    public static final String INTENT_PARAMETER_VENUE = "intent_parameter_venue";
    public static final String INTENT_PARAMETER_PROFILE = "intent_parameter_profile";
    public static final String INTENT_PARAMETER_PUSH_TOKEN = "intent_parameter_push_token";
    public static final String INTENT_PARAMETER_VIEW_PAGER_HEIGHT = "intent_parameter_view_pager_height";
    public static final String INTENT_PARAMETER_ROUTE = "intent_parameter_route";
    public static final String INTENT_PARAMETER_START_LOCATION = "intent_parameter_start_location";

    public static final String PUSH_DATA_PARAMETER_ACTION = "action";
    public static final String PUSH_DATA_PARAMETER_TITLE = "title";
    public static final String PUSH_DATA_PARAMETER_BODY = "body";

    public static final String PUSH_DATA_VALUE_USER_NOTIFY = "user_notify";
    public static final String PUSH_DATA_VALUE_CHECK_VENUE_LIST = "check_venue_list";

    public static final String REMOTE_CONFIGURATION_KEY_UPDATE_ID = "update_id";
    public static final String REMOTE_CONFIGURATION_KEY_VENUE_LIST = "venue_list";

    public static final String SHARED_PREFERENCES_KEY_USER_PROFILE = "user_profile";
    public static final String SHARED_PREFERENCES_KEY_UPDATE_ID = "update_id";
    public static final String SHARED_PREFERENCES_KEY_VENUE_LIST = "venue_list";

    public static final String JSON_OBJECT_KEY_VENUE_ID = "id";
    public static final String JSON_OBJECT_KEY_VENUE_NAME = "name";
    public static final String JSON_OBJECT_KEY_VENUE_DESCRIPTION = "description";
    public static final String JSON_OBJECT_KEY_VENUE_LATITUDE = "latitude";
    public static final String JSON_OBJECT_KEY_VENUE_LONGITUDE = "longitude";
    public static final String JSON_OBJECT_KEY_VENUE_GEOFENCE_RADIUS = "geofence_radius";
    public static final String JSON_OBJECT_KEY_VENUE_CATEGORY = "category";

    public static final String ANALYTICS_EVENT_NAME_GEOFENCE_ENTER = "GeofenceEnter";
    public static final String ANALYTICS_EVENT_NAME_GEOFENCE_EXIT = "GeofenceExit";

    public static final String ANALYTICS_EVENT_BUNDLE_KEY_VENUE_ID = "VenueId";
    public static final String ANALYTICS_EVENT_BUNDLE_KEY_VENUE_NAME = "VenueName";
    public static final String ANALYTICS_EVENT_BUNDLE_KEY_VENUE_CATEGORY = "VenueCategory";
}
