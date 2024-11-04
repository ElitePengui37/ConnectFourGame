package com.group_name.connectfourgame;

import java.io.Serializable;

public class User implements Serializable {
    public final int id;
    public String username;
    public int avatar;
    public int disc1;
    public int disc2;
    public int disc3;
    public int pvp_win_count;
    public int pvp_loss_count;
    public int pvp_draw_count;
    public int ai_win_count;
    public int ai_loss_count;
    public int ai_draw_count;

    public User(int p_id,
        String p_username, int p_avatar,
        int p_disc1, int p_disc2, int p_disc3,
        int p_pvp_win_count, int p_pvp_loss_count,
        int p_pvp_draw_count, int p_ai_win_count,
        int p_ai_loss_count, int p_ai_draw_count
    ) {
        id = p_id;
        username = p_username; avatar = p_avatar;
        disc1 = p_disc1; disc2 = p_disc2; disc3 = p_disc3;
        pvp_win_count = p_pvp_win_count;
        pvp_loss_count = p_pvp_loss_count;
        pvp_draw_count = p_pvp_draw_count;
        ai_win_count = p_ai_win_count;
        ai_loss_count = p_ai_loss_count;
        ai_draw_count = p_ai_draw_count;
    }

    public User(int p_id,
                String p_username, int p_avatar,
                int p_disc1, int p_disc2, int p_disc3
    ) {
        id = p_id;
        username = p_username; avatar = p_avatar;
        disc1 = p_disc1; disc2 = p_disc2; disc3 = p_disc3;
        pvp_win_count = 0;
        pvp_loss_count = 0;
        pvp_draw_count = 0;
        ai_win_count = 0;
        ai_loss_count = 0;
        ai_draw_count = 0;
    }

    public User editUser(
        final String p_username, final int p_avatar ,
        final int p_disc1 , final int p_disc2 , final int p_disc3
    ) {
        username = p_username;
        avatar = p_avatar;
        disc1 = p_disc1;
        disc2 = p_disc2;
        disc3 = p_disc3;

        return this;
    }

    public void copy(final User other) {
        username = other.username;
        avatar = other.avatar;
        disc1 = other.disc1;
        disc2 = other.disc2;
        disc3 = other.disc3;
        pvp_win_count = other.pvp_win_count;
        pvp_loss_count = other.pvp_loss_count;
        pvp_draw_count = other.pvp_draw_count;
        ai_win_count = other.ai_win_count;
        ai_loss_count = other.ai_loss_count;
        ai_draw_count = other.ai_draw_count;
    }
}
