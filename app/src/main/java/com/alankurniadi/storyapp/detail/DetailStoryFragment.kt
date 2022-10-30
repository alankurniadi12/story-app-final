package com.alankurniadi.storyapp.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import com.alankurniadi.storyapp.databinding.FragmentDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
            interpolator = AccelerateDecelerateInterpolator()
        }
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = DetailStoryFragmentArgs.fromBundle(arguments as Bundle).data

        with(binding) {
            Glide.with(view.context)
                .load(data.photoUrl)
                .into(ivDetailPhoto)

            tvDetailName.text = data.name
            tvDetailDescription.text = data.description
        }
    }
}
