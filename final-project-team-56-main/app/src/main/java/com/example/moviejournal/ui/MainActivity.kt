package com.example.moviejournal.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.moviejournal.BuildConfig
import com.example.moviejournal.R
import com.example.moviejournal.data.JournalEntry
import com.example.moviejournal.viewmodels.JournalViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

const val TMDB_API_KEY = BuildConfig.TMDB_API_KEY

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private val tag = "MainActivity"

    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    private lateinit var bottomNavigation: BottomNavigationView

    // Movie journal database
    private lateinit var journalViewModel: JournalViewModel

    private lateinit var recentEntriesSubMenu: SubMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        journalViewModel = ViewModelProvider(this)[JournalViewModel::class.java]

        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
//        bottomNavigation = findViewById(R.id.bottom_navigation)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, null, 0, 0
        )

        drawerLayout.addDrawerListener(toggle)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController
        setSupportActionBar(findViewById(R.id.top_app_bar))

        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.movie_search,
                R.id.journal
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfig)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        navView.setCheckedItem(R.id.drawer_menu_new_entry)


        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)

        recentEntriesSubMenu = navView.menu[1].subMenu!!

//        bottomNavigation.setOnItemSelectedListener(::onBottomNavigationItemSelected)

        journalViewModel.allJournalEntries.observe(this) { allJournalEntries ->
            updateJournalEntriesInDrawer(allJournalEntries)
        }
    }


//    private fun onBottomNavigationItemSelected(item: MenuItem): Boolean {
//        return when(item.itemId) {
//            R.id.movie_search -> {
//                navController.navigate(R.id.movie_search)
//                true
//            }
//            R.id.journal_entries -> {
//                navController.navigate(R.id.journal)
//                true
//            }
//            else -> false
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.drawer_menu_settings -> {
                navController.navigate(R.id.settings)
            }
            R.id.drawer_menu_all_entries -> {
                navController.navigate(R.id.journal)
                item.isChecked = true
            }
            R.id.drawer_menu_new_entry -> {
                navController.navigate(R.id.movie_search)
                item.isChecked = true
            }

            R.id.drawer_menu_watchlist -> {
                navController.navigate(R.id.watchlist)
                item.isChecked = true
            }
        }

        return true
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
    }

    private fun updateJournalEntriesInDrawer(allJournalEntries: List<JournalEntry>) {
        recentEntriesSubMenu.clear()

        for (index in 0..2) {
            val entry = allJournalEntries.getOrNull(index)
            if (entry != null) {
                val newMenuEntry = recentEntriesSubMenu.add(entry.movieName)
                newMenuEntry.setIcon(R.drawable.ic_action_draw)
                newMenuEntry.setOnMenuItemClickListener {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    val bundle = Bundle()
                    bundle.putSerializable("journal_entry", entry)
                    navController.navigate(R.id.entry_edit, bundle)
                    return@setOnMenuItemClickListener true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}