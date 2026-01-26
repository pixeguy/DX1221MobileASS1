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

import java.util.HashMap;

public class SoundManager extends Singleton<SoundManager> implements ObjectBase {
    private MediaPlayer bgmPlayer;
    private SoundPool soundPool;

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
                bgmPlayer.setVolume(0.5f,0.5f);
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
        soundPool.play(sound.soundID,vol,vol,1,1,1);
    }


    @Override
    public boolean handle(Message message) {
        return false;
    }
}
