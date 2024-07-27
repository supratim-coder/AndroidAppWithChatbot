package com.example.gupshup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gupshup.adapter.MessageAdapter;
import com.example.gupshup.model.Message;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatGPTFragment extends Fragment {

    RecyclerView gptRecyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton gptSendBtn;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    ImageButton gptBackBtn;
    MainActivity mainActivity;
    private ChatFutures chatModel;
    private LinearLayout chatBodyContainer;
    public static final MediaType JSON = MediaType.get("application/json");

    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    public ChatGPTFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_g_p_t, container, false);
        welcomeTextView = view.findViewById(R.id.welcome_text);
        messageEditText = view.findViewById(R.id.message_edit_text);
        gptSendBtn = view.findViewById(R.id.gpt_send_btn);
        gptBackBtn = view.findViewById(R.id.gpt_back_btn);
        chatBodyContainer = view.findViewById(R.id.chatResponseLayout);
        messageList = new ArrayList<>();

        mainActivity = (MainActivity) getActivity();

        chatModel = getChatModel();
        messageAdapter = new MessageAdapter(messageList);
//        gptRecyclerView.setAdapter(messageAdapter);
//        LinearLayoutManager llm = new LinearLayoutManager(getContext());
//        llm.setStackFromEnd(true);
//        gptRecyclerView.setLayoutManager(llm);

        gptBackBtn.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        });

        gptSendBtn.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
//            addToChat(question,Message.SENT_BY_ME);
//            messageEditText.setText("");
//            callApi(question);
            messageEditText.setText("");
            populateChatBody("You", question);
            welcomeTextView.setVisibility(View.GONE);
            GeminiPro.getResponse(chatModel, question, new ResponseCallback() {
                @Override
                public void onResponse(String response) {
                    populateChatBody("GeminiPro", response);
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.e("ChatGPTFragment", "Error: ", throwable);
                    populateChatBody("Gemini", "Sorry,I'm having a trouble understanding that!Please try again.");
                }
            });


        });

        return view;
    }

    private ChatFutures getChatModel() {
        GeminiPro model = new GeminiPro();
        GenerativeModelFutures modelFutures = model.getModel();

        return modelFutures.startChat();
    }

    public void populateChatBody(String userName, String message) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.chat_message_block, null);

        TextView userAgentName = view1.findViewById(R.id.username_text_view);
        TextView userAgentMessage = view1.findViewById(R.id.user_message_text_view);

        userAgentName.setText(userName);
        userAgentMessage.setText(message);

        chatBodyContainer.addView(view1);

        ScrollView scrollView = getView().findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}


//    void addToChat(String message,String sentBy){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Background work here
//
//
//                // Update UI on the UI thread
//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // UI update code here
//                            messageList.add(new Message(message,sentBy));
//                            messageAdapter.notifyDataSetChanged();
//                            gptRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
//    public void addResponse(String response){
//        messageList.remove(messageList.size()-1);
//        addToChat(response,Message.SENT_BY_BOT);
//    }
//    void callApi(String question){
//
//        messageList.add(new Message("Typing....",Message.SENT_BY_BOT));
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("model","gpt-3.5-turbo");
//
//            JSONArray messageArr = new JSONArray();
//            JSONObject obj = new JSONObject();
//            obj.put("role", "user");
//            obj.put("content", question);
//
//            messageArr.put(obj);
//
//            jsonBody.put("messages",messageArr);
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
//        Request request = new Request.Builder()
//                .url("https://api.openai.com/v1/chat/completions")
//                .header("Authorization","Bearer sk-proj-iL6KYN7XZYbtsMmboF4HT3BlbkFJQkUufxgUWrZsF9PImQ4P")
//                .post(body)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                addResponse("Failed to load response due to "+e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if(response.isSuccessful()){
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
//                        String result = jsonArray.getJSONObject(0)
//                                .getJSONObject("message")
//                                .getString("content");
//                        addResponse(result.trim());
//
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                }else{
//                    addResponse("Failed to load response due to "+response.body().toString());
//                }
//            }
//        });
//    }
//}



//package com.example.gupshup;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.gupshup.adapter.MessageAdapter;
//import com.example.gupshup.model.Message;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import java.util.concurrent.TimeUnit;
//
//
//public class ChatGPTFragment extends Fragment {
//
//    RecyclerView gptRecyclerView;
//    TextView welcomeTextView;
//    EditText messageEditText;
//    ImageButton gptSendBtn;
//    List<Message> messageList;
//    MessageAdapter messageAdapter;
//    ImageButton gptBackBtn;
//    MainActivity mainActivity;
//    private LinearLayout chatBodyContainer;
//    private String stringURLEndPoint="https://api.openai.com/v1/chat/completions";
//    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
//
//    OkHttpClient client = new OkHttpClient.Builder()
//            .readTimeout(60, TimeUnit.SECONDS)
//            .build();
//
//    public ChatGPTFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_chat_g_p_t, container, false);
//        gptRecyclerView = view.findViewById(R.id.gpt_recycler_view);
//        welcomeTextView = view.findViewById(R.id.welcome_text);
//        messageEditText = view.findViewById(R.id.message_edit_text);
//        gptSendBtn = view.findViewById(R.id.gpt_send_btn);
//        gptBackBtn = view.findViewById(R.id.gpt_back_btn);
////        chatBodyContainer = view.findViewById(R.id.chat_response_layout);
//        messageList = new ArrayList<>();
//
//        mainActivity = (MainActivity) getActivity();
//
////        chatModel = getChatModel();
//        //setup recyclerview
//        messageAdapter = new MessageAdapter(messageList);
//        gptRecyclerView.setAdapter(messageAdapter);
//        LinearLayoutManager llm = new LinearLayoutManager(getContext());
//        llm.setStackFromEnd(true);
//        gptRecyclerView.setLayoutManager(llm);
//
//        gptBackBtn.setOnClickListener(v -> {
//            Intent i = new Intent(getActivity(), MainActivity.class);
//            startActivity(i);
//        });
//
//        gptSendBtn.setOnClickListener(v -> {
//            String question = messageEditText.getText().toString().trim();
//            addToChat(question, Message.SENT_BY_ME);
//            messageEditText.setText("");
//            callApi(question);
//            welcomeTextView.setVisibility(View.GONE);
//        });
//
//        return view;
//    }
//
//    void addToChat(String message, String sentBy) {
//        getActivity().runOnUiThread(() -> {
//            messageList.add(new Message(message, sentBy));
//            messageAdapter.notifyDataSetChanged();
//            gptRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//        });
//    }
//
//    public void addResponse(String response) {
//        getActivity().runOnUiThread(() -> {
//            messageList.remove(messageList.size() - 1);
//            addToChat(response, Message.SENT_BY_BOT);
//        });
//    }
//
////    void callApi(String question) {
////        addToChat("Typing....", Message.SENT_BY_BOT);
////
////        JSONObject jsonBody = new JSONObject();
////        try {
////            jsonBody.put("model", "gpt-3.5-turbo");
////
////            JSONArray messageArr = new JSONArray();
////            JSONObject obj = new JSONObject();
////            obj.put("role", "user");
////            obj.put("content", question);
////
////            messageArr.put(obj);
////
////            jsonBody.put("messages", messageArr);
////
////        } catch (JSONException e) {
////            addResponse("Failed to create request: " + e.getMessage());
////            return;
////        }
////
////        // Encode API key
////        String apiKey = "sk-proj-iL6KYN7XZYbtsMmboF4HT3BlbkFJQkUufxgUWrZsF9PImQ4P";  // Replacing it  with actual API key
////        String encodedApiKey = "Bearer " + apiKey;
////        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
////        Request request = new Request.Builder()
////                .url(stringURLEndPoint)
////                .header("Authorization", encodedApiKey) // Make sure to replace with your actual API key
////                .post(body)
////                .build();
////
////        client.newCall(request).enqueue(new Callback() {
////            @Override
////            public void onFailure(@NonNull Call call, @NonNull IOException e) {
////                addResponse("Failed to load response due to " + e.getMessage());
////            }
////
////            @Override
////            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
////                if (response.isSuccessful()) {
////                    try {
////                        JSONObject jsonObject = new JSONObject(response.body().string());
////                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
////                        String result = jsonArray.getJSONObject(0)
////                                .getJSONObject("message")
////                                .getString("content");
////                        addResponse(result.trim());
////                    } catch (JSONException e) {
////                        addResponse("Failed to parse response: " + e.getMessage());
////                    }
////                } else {
////                    addResponse("Failed to load response: " + response.code());
////                }
////            }
////        });
////    }
//void callApi(String question) {
//    addToChat("Typing....", Message.SENT_BY_BOT);
//
//    JSONObject jsonBody = new JSONObject();
//    try {
//        jsonBody.put("model", "gpt-3.5-turbo");
//
//        JSONArray messageArr = new JSONArray();
//        JSONObject obj = new JSONObject();
//        obj.put("role", "user");
//        obj.put("content", question);
//
//        messageArr.put(obj);
//
//        jsonBody.put("messages", messageArr);
//
//    } catch (JSONException e) {
//        addResponse("Failed to create request: " + e.getMessage());
//        return;
//    }
//
//    // Encode API key
//    String apiKey = "sk-proj-iL6KYN7XZYbtsMmboF4HT3BlbkFJQkUufxgUWrZsF9PImQ4P";  // Replace with your actual API key
//    String encodedApiKey = "Bearer " + apiKey;
//
//    RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
//    Request request = new Request.Builder()
//            .url(stringURLEndPoint)
//            .header("Authorization", encodedApiKey)
//            .post(body)
//            .build();
//
//    executeRequestWithRetry(request, 3, 2); // 3 attempts with 2 seconds initial delay
//}
//
//    private void executeRequestWithRetry(Request request, int maxAttempts, int initialDelay) {
//        client.newCall(request).enqueue(new Callback() {
//            int attempts = 1;
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                if (attempts < maxAttempts) {
//                    attempts++;
//                    try {
//                        Thread.sleep(initialDelay * 1000 * (long) Math.pow(2, attempts - 1)); // Exponential backoff
//                    } catch (InterruptedException interruptedException) {
//                        Thread.currentThread().interrupt();
//                    }
//                    client.newCall(request).enqueue(this); // Retry the request
//                } else {
//                    addResponse("Failed to load response due to " + e.getMessage());
//                }
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
//                        String result = jsonArray.getJSONObject(0)
//                                .getJSONObject("message")
//                                .getString("content");
//                        addResponse(result.trim());
//                    } catch (JSONException e) {
//                        addResponse("Failed to parse response: " + e.getMessage());
//                    }
//                } else if (response.code() == 429) { // Rate limit error
//                    if (attempts < maxAttempts) {
//                        attempts++;
//                        try {
//                            Thread.sleep(initialDelay * 1000 * (long) Math.pow(2, attempts - 1)); // Exponential backoff
//                        } catch (InterruptedException interruptedException) {
//                            Thread.currentThread().interrupt();
//                        }
//                        client.newCall(request).enqueue(this); // Retry the request
//                    } else {
//                        addResponse("Failed to load response due to rate limit (429)");
//                    }
//                } else {
//                    addResponse("Failed to load response: " + response.code());
//                }
//            }
//        });
//    }
//}
