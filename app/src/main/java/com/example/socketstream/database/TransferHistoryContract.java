package com.example.socketstream.database;

import android.provider.BaseColumns;

public class TransferHistoryContract  {

    // To prevent someone from accidentally
    // instantiating the contract class,
    // give it an empty constructor.
    private TransferHistoryContract() {}

    /*
    Receive text message history database table constants
     */
    public static final class ReceiveTextMessage implements BaseColumns{
        /*
    Constants for receive text message history table

     */

        //Table Name
        public static final String RECEIVE_TEXT_TABLE_NAME="receivetext";

        //Table columns
        public static final String RECEIVE_TEXT_ID=BaseColumns._ID;
        public static final String RECEIVE_TEXT_MESSAGE_COLUMN="receivetextmessage";
    }

    /*
    database table constanst for received images
     */
    public static final class ReceiveImages1 implements BaseColumns{
        public final static String RECEIVE_IMAGES_TABLE_NAME="receiveimages";

        public static final String RECEIVE_IMAGES_ID=BaseColumns._ID;
        public static final String RECEIVE_IMAGES_COLUMN_URI="imageuri";

    }


}
