package br.com.pedroadmn.aceplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.pedroadmn.aceplayer.R.id
import br.com.pedroadmn.aceplayer.R.layout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        setSupportActionBar(toolbar)

        navController = findNavController(id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(setOf(id.homeFragment, id.exploreFragment, id.postFragment, id.subscriptionFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigation?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}