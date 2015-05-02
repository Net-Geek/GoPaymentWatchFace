/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intuit.gopayment.android.watchface;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public final class DigitalWatchFaceUtil {
    private static final String TAG = "DigitalWatchFaceUtil";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} background color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_BACKGROUND_COLOR = "BACKGROUND_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} hour digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_HOURS_COLOR = "HOURS_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} minute digits color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_MINUTES_COLOR = "MINUTES_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} colon color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_COLON_COLOR = "COLON_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} total color name.
     * The color name must be a {@link String} recognized by {@link Color#parseColor}.
     */
    public static final String KEY_TOTAL_COLOR = "TOTAL_COLOR";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} total value.
     */
    public static final String KEY_DAILY_TOTAL = "DAILY_TOTAL";

    /**
     * The {@link DataMap} key for {@link DigitalWatchFaceService} goal value.
     */
    public static final String KEY_DAILY_GOAL = "DAILY_GOAL";


    /**
     * The path for the {@link DataItem} containing {@link DigitalWatchFaceService} configuration.
     */
    public static final String PATH_WITH_FEATURE = "/watch_face_config/Digital";

    /**
     * Name of the default interactive mode background color and the ambient mode background color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_BACKGROUND = "Black";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_BACKGROUND =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_BACKGROUND);

    /**
     * Name of the default interactive mode goal wave color and the ambient mode goal wave color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_GOAL_WAVE = "#3779F7";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_GOAL_WAVE =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_GOAL_WAVE);

    /**
     * Name of the default interactive mode goal wave color and the ambient mode goal wave color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_GOAL_MET_WAVE = "#56b349";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_GOAL_MET_WAVE =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_GOAL_MET_WAVE);

    /**
     * Name of the default interactive mode hour digits color and the ambient mode hour digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_HOUR_DIGITS = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_HOUR_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_HOUR_DIGITS);

    /**
     * Name of the default interactive mode minute digits color and the ambient mode minute digits
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_MINUTE_DIGITS = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_MINUTE_DIGITS =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_MINUTE_DIGITS);

    /**
     * Name of the default interactive mode colon color and the ambient mode colon
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_COLON = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_COLON =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_COLON);

    /**
     * Name of the default interactive mode total color and the ambient mode total
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_TOTAL = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_TOTAL =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_TOTAL);

    /**
     * Name of the default interactive mode date color and the ambient mode date
     * color.
     */
    public static final String COLOR_NAME_DEFAULT_AND_AMBIENT_DATE = "White";
    public static final int COLOR_VALUE_DEFAULT_AND_AMBIENT_DATE =
            parseColor(COLOR_NAME_DEFAULT_AND_AMBIENT_DATE);

    /**
     * Default value for the daily total.
     */
    public static final int DAILY_TOTAL_DEFAULT = 0;

    /**
     * Default value for the daily goal.
     */
    public static final int DAILY_GOAL_DEFAULT = 1000;

    /**
     * Callback interface to perform an action with the current config {@link DataMap} for
     * {@link DigitalWatchFaceService}.
     */
    public interface FetchConfigDataMapCallback {
        /**
         * Callback invoked with the current config {@link DataMap} for
         * {@link DigitalWatchFaceService}.
         */
        void onConfigDataMapFetched(DataMap config);
    }

    private static int parseColor(String colorName) {
        return Color.parseColor(colorName.toLowerCase());
    }

    /**
     * Asynchronously fetches the current config {@link DataMap} for {@link DigitalWatchFaceService}
     * and passes it to the given callback.
     * <p>
     * If the current config {@link DataItem} doesn't exist, it isn't created and the callback
     * receives an empty DataMap.
     */
    public static void fetchConfigDataMap(final GoogleApiClient client,
            final FetchConfigDataMapCallback callback) {
        Wearable.NodeApi.getLocalNode(client).setResultCallback(
                new ResultCallback<NodeApi.GetLocalNodeResult>() {
                    @Override
                    public void onResult(NodeApi.GetLocalNodeResult getLocalNodeResult) {
                        String localNode = getLocalNodeResult.getNode().getId();
                        Uri uri = new Uri.Builder()
                                .scheme("wear")
                                .path(DigitalWatchFaceUtil.PATH_WITH_FEATURE)
                                .authority(localNode)
                                .build();
                        Wearable.DataApi.getDataItem(client, uri)
                                .setResultCallback(new DataItemResultCallback(callback));
                    }
                }
        );
    }

    /**
     * Overwrites (or sets, if not present) the keys in the current config {@link DataItem} with
     * the ones appearing in the given {@link DataMap}. If the config DataItem doesn't exist,
     * it's created.
     * <p>
     * It is allowed that only some of the keys used in the config DataItem appear in
     * {@code configKeysToOverwrite}. The rest of the keys remains unmodified in this case.
     */
    public static void overwriteKeysInConfigDataMap(final GoogleApiClient googleApiClient,
            final DataMap configKeysToOverwrite) {

        DigitalWatchFaceUtil.fetchConfigDataMap(googleApiClient,
                new FetchConfigDataMapCallback() {
                    @Override
                    public void onConfigDataMapFetched(DataMap currentConfig) {
                        DataMap overwrittenConfig = new DataMap();
                        overwrittenConfig.putAll(currentConfig);
                        overwrittenConfig.putAll(configKeysToOverwrite);
                        DigitalWatchFaceUtil.putConfigDataItem(googleApiClient, overwrittenConfig);
                    }
                }
        );
    }

    /**
     * Overwrites the current config {@link DataItem}'s {@link DataMap} with {@code newConfig}.
     * If the config DataItem doesn't exist, it's created.
     */
    public static void putConfigDataItem(GoogleApiClient googleApiClient, DataMap newConfig) {
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH_WITH_FEATURE);
        DataMap configToPut = putDataMapRequest.getDataMap();
        configToPut.putAll(newConfig);
        Wearable.DataApi.putDataItem(googleApiClient, putDataMapRequest.asPutDataRequest())
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (Log.isLoggable(TAG, Log.DEBUG)) {
                            Log.d(TAG, "putDataItem result status: " + dataItemResult.getStatus());
                        }
                    }
                });
    }

    private static class DataItemResultCallback implements ResultCallback<DataApi.DataItemResult> {

        private final FetchConfigDataMapCallback mCallback;

        public DataItemResultCallback(FetchConfigDataMapCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onResult(DataApi.DataItemResult dataItemResult) {
            if (dataItemResult.getStatus().isSuccess()) {
                if (dataItemResult.getDataItem() != null) {
                    DataItem configDataItem = dataItemResult.getDataItem();
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(configDataItem);
                    DataMap config = dataMapItem.getDataMap();
                    mCallback.onConfigDataMapFetched(config);
                } else {
                    mCallback.onConfigDataMapFetched(new DataMap());
                }
            }
        }
    }

    private DigitalWatchFaceUtil() { }
}
