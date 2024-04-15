package com.example.splash_screen.View.screen_account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.splash_screen.R
import com.example.splash_screen.constans.const
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class FragmentAccount : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var gmail: TextView
    private lateinit var image: ImageView

    private lateinit var logout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        auth = FirebaseAuth.getInstance()
        gmail = view.findViewById(R.id.uid)
        image = view.findViewById(R.id.iamge)
        logout = view.findViewById(R.id.button)

        val currentUser = auth.currentUser

        try {
            gmail.text = currentUser?.email
            Picasso.get().load(currentUser?.photoUrl).into(image)
            Log.e("url", currentUser?.photoUrl.toString())
        }catch (e: FirebaseException){
            Log.e("exception", e.toString())
        }

        logout.setOnClickListener {
            auth.signOut()
            //replaceFragment(NoLoginFragment())
            const.selectedIndex = R.id.account
            requireActivity().recreate()
        }
        return view
    }
}