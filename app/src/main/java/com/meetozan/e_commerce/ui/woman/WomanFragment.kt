package com.meetozan.e_commerce.ui.woman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.meetozan.e_commerce.data.dto.ProductDto
import com.meetozan.e_commerce.databinding.FragmentWomanBinding
import com.meetozan.e_commerce.ui.adapter.ProductAdapter
import com.meetozan.e_commerce.ui.favorites.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WomanFragment : Fragment() {

    private var _binding: FragmentWomanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductAdapter
    private lateinit var rv: RecyclerView
    private val viewModel: WomanViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWomanBinding.inflate(inflater, container, false)
        observer()
        return binding.root
    }

    private fun observer() {
        viewModel.womanList.observe(viewLifecycleOwner) {
            adapter = ProductAdapter(it as MutableList<ProductDto>,requireContext(),layoutInflater,favoritesViewModel)
            rv = binding.womanRv
            rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            rv.adapter = adapter
        }
    }
}