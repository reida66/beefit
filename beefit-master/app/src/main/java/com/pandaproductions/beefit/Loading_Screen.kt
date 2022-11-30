package com.pandaproductions.beefit

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.pandaproductions.beefit.Database.AppDatabase
import com.pandaproductions.beefit.Models.User
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick


class Loading_Screen : AppCompatActivity() {
    var animBlink: Animation? = null
    lateinit var beePhoto: ImageView
    lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading__screen)
        beePhoto = findViewById(R.id.bee)
        animBlink = AnimationUtils.loadAnimation(
            getApplicationContext(),
            R.anim.bounce
        );
        beePhoto.startAnimation(animBlink)

        button = findViewById(R.id.start_Button)
        button.onClick {
            doAsync {
                // check if user exists, run setup if they do not
                val users: List<User> = AppDatabase.getInstance(this@Loading_Screen).userDao().getUsers()
                if (users.isEmpty()) { // setup user if no user info in db
                    val intent = Intent(this@Loading_Screen, AppSetupActivity::class.java)
                    startActivityForResult(intent, 0)
                } else {
                    val intent = Intent(this@Loading_Screen, MainActivity::class.java)
                    startActivityForResult(intent, 0)
                }
            }
        }


    }
}
