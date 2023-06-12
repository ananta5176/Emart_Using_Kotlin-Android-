package com.meetozan.e_commerce.ui.shopping_cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meetozan.e_commerce.R
import com.meetozan.e_commerce.data.dto.ProductDto
import com.meetozan.e_commerce.databinding.FragmentShoppingCartBinding
import com.meetozan.e_commerce.ui.adapter.CartItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingCartFragment : Fragment() {

    private var _binding: FragmentShoppingCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartItemAdapter
    private lateinit var rv: RecyclerView
    private val viewModel: ShoppingCartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        observer()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = binding.shoppingCartRv
        rv.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = linearLayoutManager

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.main_graph)
            }
        }

        binding.btnGoHome.setOnClickListener {
            it.findNavController().navigate(R.id.homeFragment)
        }

        binding.btnConfirmCart.setOnClickListener {
            it.findNavController().navigate(R.id.action_shoppingCartFragment_to_addressFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    fun observer() {
        viewModel.basketList.observe(viewLifecycleOwner) {
            adapter = CartItemAdapter(it as MutableList<ProductDto>, viewModel, requireContext())
            rv.adapter = adapter

            if (it.isNotEmpty()) {
                binding.cvShoppingCartTotal.visibility = View.VISIBLE
                binding.cvSearchNotFound.visibility = View.GONE
                binding.shoppingCartRv.visibility = View.VISIBLE

                var totalPrice = 0

                for (index in it.indices) {
                    totalPrice += it[index].price * it[index].piece
                }

                binding.tvTotalPrice.text = totalPrice.toString()

            } else {
                binding.cvSearchNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}