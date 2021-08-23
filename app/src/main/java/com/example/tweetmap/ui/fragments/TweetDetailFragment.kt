package com.example.tweetmap.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tweetmap.databinding.FragmentTweetDetailBinding

class TweetDetailFragment : Fragment() {

    private lateinit var binding: FragmentTweetDetailBinding
    val args: TweetDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTweetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTweet.text = args.tweet

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }
}