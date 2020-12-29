package ru.csu.videochat.model.utilities;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final String KEY_COLLECTION_CATEGORIES = "categories";
    public static final String KEY_COLLECTION_CHATS = "chats";

    public static final String KEY_CATEGORY_ID = "category";
    public static final String KEY_CHAT_ID = "chat_id";
    public static final String KEY_USER_ID = "uidToken";
    public static final String KEY_AGE_ID = "yourAge";

    public static final String KEY_CHAT_UID_FIRST_TOKEN = "uid1Token";
    public static final String KEY_CHAT_UID_SECOND_TOKEN = "uid2Token";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_PREFERENCE_NAME_TOKEN = "serverToken";
    public static final String KEY_PREFERENCE_NAME_AVATAR = "avatar";
    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";

    public static final String REMOTE_MSG_INVITATION = "invitation";

    public static final String REMOTE_MSG_INVITER_TOKEN = "inviterToken";
    public static final String REMOTE_MSG_DATA = "data";

    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_MEETING_ROOM = "meetingRoom";

    public static final String KEY_YOUR_AGE = "your_age";
    public static final String KEY_COMPANION_AGES = "companion_age";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_AVATAR = "image";
    public static final String KEY_LINK = "link";

    public static final String SALT_FIRST = "Qd9#hd$y5s";
    public static final String SALT_SECOND = "saq2$y83s";

    public static List<Pair<String, String>> getCategory() {
        List<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
        pairs.add(new Pair<>("Спорт", "https://gtrk-saratov.ru/wp-content/uploads/2019/12/5f2f983b1d838ee26be6b93a38fa378c.jpg"));
        pairs.add(new Pair<>("Кино", "https://cdn22.img.ria.ru/images/91548/33/915483359_0:203:3888:2390_600x0_80_0_0_d82704e34388d68bdcf57766e7b0b361.jpg"));
        pairs.add(new Pair<>("Игры", "https://www.goodnewsfinland.com/wp-content/uploads/2019/08/Games-765x430.png"));
        pairs.add(new Pair<>("Кулинария", "https://recipesbook.ru/uploads/posts/2012-08/1345130069_finskaya-kuhnya.jpeg"));
        pairs.add(new Pair<>("Литература", "https://storage.theoryandpractice.ru/tnp/uploads/image_unit/000/132/309/image/base_93959877b0.jpg"));
        pairs.add(new Pair<>("Музыка", "https://www.iguides.ru/upload/iblock/3a0/3a0569a7237bbfb5406580deed8a9958.jpg"));
        return pairs;
    }

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION,
                "key=AAAAr8AqImI:APA91bHtpbIC41x-8k-1N__uEoXTbM9UlgpcfX06T0s_w6EylnzE1lqEGuTmlgyr9bhxtoDW0G9s-0qBo5aGdJOpUp6R-k-4tfA9gddqBZoviTZCu9boPI1Vi0QgrkJrrYEeuZkcyaS2");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;
    }
}
