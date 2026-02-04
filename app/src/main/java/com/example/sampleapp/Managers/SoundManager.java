package com.example.sampleapp.Managers;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.sampleapp.Enums.SoundList;
import com.example.sampleapp.PostOffice.Message;
import com.example.sampleapp.PostOffice.ObjectBase;
import com.example.sampleapp.PostOffice.PostOffice;
import com.example.sampleapp.R;
import com.example.sampleapp.mgp2d.core.GameActivity;
import com.example.sampleapp.mgp2d.core.Singleton;
import com.example.sampleapp.mgp2d.core.Vector2;

import java.util.HashMap;
import java.util.Random;

public class SoundManager extends Singleton<SoundManager> implements ObjectBase {
    private MediaPlayer bgmPlayer;
    private SoundPool soundPool;

    private float masterVolume = 1.0f;
    private float musicVolume = 1.0f;
    private float orignalBgmVol = 0.5f;

    public static SoundManager getInstance() { return Singleton.getInstance(SoundManager.class);}
   public void startMusic() {
        if(bgmPlayer != null && !bgmPlayer.isPlaying())
        {
            bgmPlayer.start();
        }
   }

   public void pauseSounds()
   {
       if (bgmPlayer != null)
           bgmPlayer.pause();
       if (soundPool != null)
           soundPool.autoPause();
   }

   public void resumeSounds()
   {
       if (bgmPlayer != null)
           bgmPlayer.start();
       if (soundPool != null)
           soundPool.autoResume();
   }

    public void InitAudio(android.content.Context ctx) {
        if(bgmPlayer == null)
        {
            bgmPlayer = MediaPlayer.create(ctx, SoundList.Bgm.resourceID);
            if (bgmPlayer != null)
            {
                bgmPlayer.setLooping(true);
                float volume = orignalBgmVol * masterVolume * musicVolume;
                bgmPlayer.setVolume(volume,volume);
            }
        }

        if (soundPool == null)
        {
            AudioAttributes attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attrs)
                    .setMaxStreams(4)
                    .build();

            for(SoundList s : SoundList.values())
            {
                s.load(soundPool, ctx);
            }
            soundPool.setOnLoadCompleteListener((pool, sampleId,status) -> {
                if (status != 0) return;
                for (SoundList s : SoundList.values())
                {
                    s.markLoaded(sampleId);
                }
            });
        }
    }

    public void stopAudio() {
        if(bgmPlayer != null && bgmPlayer.isPlaying())
        {
            bgmPlayer.stop();
            bgmPlayer.release();
            bgmPlayer = null;
        }

        if (soundPool != null)
        {
            soundPool.release();
            soundPool = null;
            for (SoundList s : SoundList.values())
            {
                s.loaded = false;
                s.soundID = 0;
            }
        }
    }

    public void PlayAudio(SoundList sound, float vol)
    {
        if (!sound.loaded) return;
        float finalV = vol * masterVolume;
        soundPool.play(sound.soundID,finalV,finalV,1,1,1);
    }

    public void PlayAudio(SoundList sound, float vol, float pitch, float pitchVariance)
    {
        if (!sound.loaded) return;

        // Define the variance range (e.g., +/- 0.1)
        Random random = new Random();

        // Calculate a random pitch between (pitch - variance) and (pitch + variance)
        // Formula: min + random * (max - min)
        float minPitch = pitch - pitchVariance;
        float maxPitch = pitch + pitchVariance;
        float finalPitch = minPitch + random.nextFloat() * (maxPitch - minPitch);

        // Ensure finalPitch stays within SoundPool's supported range (0.5 to 2.0)
        finalPitch = Math.max(0.5f, Math.min(2.0f, finalPitch));

        float finalV = vol * masterVolume;
        soundPool.play(sound.soundID, finalV, finalV, 1, 0, pitch);
    }

    public void PlayAudioAtPosition(SoundList sound, Vector2 sourcePos, Vector2 listenerPos, float maxDistance) {
        // 1. Calculate distance
        float distance = sourcePos.subtract(listenerPos).getMagnitude();

        // 2. Calculate volume (1.0 at 0 distance, 0.0 at maxDistance)
        float vol = 1.0f - (distance / maxDistance);

        // Clamp volume between 0 and 1
        if (vol < 0) vol = 0;
        if (vol > 1) vol = 1;

        // 3. Add a small random pitch for variety
        float randomPitch = 0.95f + (new java.util.Random().nextFloat() * 0.1f);

        // 4. Play only if audible
        if (vol > 0.01f) {
            PlayAudio(sound, vol, 1.0f, randomPitch);
        }
    }

    @Override
    public boolean handle(Message message) {
        return false;
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMasterVolume(float v) {
        masterVolume = v;
        bgmPlayer.setVolume(orignalBgmVol * masterVolume * musicVolume,
                           orignalBgmVol * masterVolume * musicVolume);
    }

    public void setMusicVolume(float v) {
        musicVolume = v;
        bgmPlayer.setVolume(orignalBgmVol * masterVolume * musicVolume,
                           orignalBgmVol * masterVolume * musicVolume);
    }
}
