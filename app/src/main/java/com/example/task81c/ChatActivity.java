package com.example.task81c;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout chatLayout;
    private ScrollView chatScrollView;
    private EditText messageInput;
    private ImageButton sendButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatLayout = findViewById(R.id.chatLayout);
        chatScrollView = findViewById(R.id.chatScrollView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        username = getIntent().getStringExtra("username");

        addMessage("Welcome " + username + "!", false);

        sendButton.setOnClickListener(v -> {
            String msg = messageInput.getText().toString().trim();
            if (!msg.isEmpty()) {
                addMessage(msg, true);

                ChatRequest request = new ChatRequest(msg);
                ApiService apiService = ApiClient.getClient().create(ApiService.class);

                apiService.sendMessage(request).enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            addMessage(response.body().getBot(), false);
                        } else {
                            addMessage("Server Error: " + response.code(), false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {
                        addMessage("Connection failed: " + t.getMessage(), false);
                    }
                });

                messageInput.setText("");
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(24, 16, 24, 16);
        textView.setTextSize(16);
        textView.setBackgroundResource(isUser ? android.R.color.holo_green_light : android.R.color.darker_gray);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        textView.setLayoutParams(params);
        chatLayout.addView(textView);

        // ✅ Scroll to bottom after adding new message
        chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
    }
}

