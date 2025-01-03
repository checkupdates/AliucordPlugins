package com.aliucord.plugin;

import android.content.Context;
import com.aliucord.Plugin;
import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.utils.RxUtils;
import com.discord.api.commands.ApplicationCommandOptionType;
import com.discord.stores.StoreStream;
import com.discord.utilities.rest.RestAPI;

@AliucordPlugin
public class FakeMuteDeafenPlugin extends Plugin {

    @Override
    public void start(Context context) {
        // Tambahkan perintah untuk mengaktifkan/mematikan fake mute
        commands.registerCommand(
            "fakemute",
            "Toggle fake mute (terlihat oleh pengguna lain)",
            Collections.emptyList(),
            ctx -> {
                boolean newMuteState = !StoreStream.getVoiceState().getSelfMute();
                setMuteState(newMuteState);
                return newMuteState ? "Fake Mute diaktifkan" : "Fake Mute dinonaktifkan";
            }
        );

        // Tambahkan perintah untuk mengaktifkan/mematikan fake deafen
        commands.registerCommand(
            "fakedeafen",
            "Toggle fake deafen (terlihat oleh pengguna lain)",
            Collections.emptyList(),
            ctx -> {
                boolean newDeafenState = !StoreStream.getVoiceState().getSelfDeaf();
                setDeafenState(newDeafenState);
                return newDeafenState ? "Fake Deafen diaktifkan" : "Fake Deafen dinonaktifkan";
            }
        );
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }

    private void setMuteState(boolean mute) {
        RxUtils.subscribe(
            RestAPI.patchVoiceState(
                StoreStream.getVoiceChannel().getCurrentChannelId(),
                mute,
                StoreStream.getVoiceState().getSelfDeaf()
            )
        );
    }

    private void setDeafenState(boolean deafen) {
        RxUtils.subscribe(
            RestAPI.patchVoiceState(
                StoreStream.getVoiceChannel().getCurrentChannelId(),
                StoreStream.getVoiceState().getSelfMute(),
                deafen
            )
        );
    }
    }
