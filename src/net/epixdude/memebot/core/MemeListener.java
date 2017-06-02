package net.epixdude.memebot.core;

import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

/**
 * An implementation of ConnectionListener designed to send
 * the instance of MemeBot a signal to disconnect.
 * To do this, we use OnStatusChange.
 * @author Daniel Cormier
 * @author Cosmo Viola
 */
class MemeListener implements ConnectionListener{

    Guild guild;

    MemeBot memes;

    public MemeListener(Guild g, MemeBot m){
        guild=g;
        memes=m;
    }

    /**
     * Execute code on change in ping (presumably).
     * Note: currently unused
     * @param arg0 the ping to discord
     */
    @Override
    public void onPing(long arg0) { 
    }

    /**
     * Execute code when the ConnectionStatus changes.
     * This is used to determine when the bot should try to disconnect
     * from the voice channel. When the bot tries to disconnect from the
     * voice channel before being connected, the bot becomes stuck in the voice
     * channel.
     * @param arg0
     */
    @Override
    public void onStatusChange(ConnectionStatus arg0) {
        // if we are connected
        if(arg0.equals(ConnectionStatus.CONNECTED)){
            // tell the disconnect thread to disconnect
            synchronized(MemeBot.lock){
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                MemeBot.dc = true;
                MemeBot.lock.notifyAll();
            }
        }
        
    }

    /**
     * Execute code when a user starts speaking.
     * @param arg0 the user speaking
     * @param arg1 if the user is speaking or not
     */
    @Override
    public void onUserSpeaking(User arg0, boolean arg1) {
    }

    /**
     * @return the guild
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * @param guild the guild to set
     */
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    /**
     * @return the memes
     */
    public MemeBot getMemes() {
        return memes;
    }

    /**
     * @param memes the memes to set
     */
    public void setMemes(MemeBot memes) {
        this.memes = memes;
    }

}