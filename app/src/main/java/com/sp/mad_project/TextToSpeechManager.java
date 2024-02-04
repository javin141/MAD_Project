package com.sp.mad_project;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TextToSpeechManager implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private boolean isTTSPlaying = false;

    public TextToSpeechManager(Context context) {
        textToSpeech = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int langResult = textToSpeech.setLanguage(Locale.getDefault());

            if (langResult == TextToSpeech.LANG_MISSING_DATA ||
                    langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeechManager", "Text to speech not supported in this language");
            }
        } else {
            Log.e("TextToSpeechManager", "Text to speech initialization failed");
        }
    }

    public void toggleTextToSpeech(String text) {
        if (isTTSPlaying) {
            stopTextToSpeech();
        } else {
            startTextToSpeech(text);
        }
    }

    private void startTextToSpeech(String text) {
        speak(text);
        isTTSPlaying = true;
    }

    private void stopTextToSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        isTTSPlaying = false;
    }

    private void speak(String text) {
        if (textToSpeech != null) {
            if (text.isEmpty()) {
                Log.e("TextToSpeechManager", "Text is empty");
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "RecipeDescription");
                } else {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "RecipeDescription");
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
                }
            }
        }
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}