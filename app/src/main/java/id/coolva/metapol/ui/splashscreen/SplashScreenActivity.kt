package id.coolva.metapol.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import id.coolva.metapol.R
import id.coolva.metapol.databinding.ActivitySplashscreenBinding
import id.coolva.metapol.ui.auth.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val handler = Handler()
//        handler.postDelayed({
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, 1000)

        binding.apply {
            ivMetapolLogo.alpha = 0f
            tvMetapol.alpha = 0f

            tvMetapol.animate().setDuration(1000).alpha(1f)
            ivMetapolLogo.animate().setDuration(1000).alpha(1f).withEndAction {
                val moveToMainActivity = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(moveToMainActivity)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}