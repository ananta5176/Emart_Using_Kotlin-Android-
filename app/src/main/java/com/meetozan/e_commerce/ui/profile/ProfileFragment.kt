package com.meetozan.e_commerce.ui.profile

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.meetozan.e_commerce.R
import com.meetozan.e_commerce.data.model.model.Product
import com.meetozan.e_commerce.databinding.FragmentProfileBinding
import com.meetozan.e_commerce.ui.adapter.ProductAdapter
import com.meetozan.e_commerce.ui.favorites.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var adapterHistory: ProductAdapter
    private lateinit var adapterCurrent: ProductAdapter
    private lateinit var rvOrderCurrent: RecyclerView
    private lateinit var rvOrderHistory: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userObserver()

        orderObserver()

        binding.imageCurrentOrders.playAnimation()

        binding.imageNoCurrentOrders.playAnimation()

        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
            it.findNavController().apply {
                popBackStack(R.id.nav_graph,true)
                navigate(R.id.nav_graph)
            }
        }

        binding.btnGoHome.setOnClickListener {
            it.findNavController().navigate(R.id.homeFragment)
        }

        binding.profileTopPlaceholder.setOnClickListener {
            return@setOnClickListener
        }

        binding.profileImageView.setOnClickListener {
            return@setOnClickListener
        }

        binding.btnOpenOrders.setOnClickListener {
            if (binding.expandableLayoutOrders.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(
                    binding.cvOrders, AutoTransition()
                )
                binding.expandableLayoutOrders.visibility = View.VISIBLE
                binding.btnOpenOrders.animate().apply {
                    duration = 500
                    rotationX(180f)
                }.start()
                return@setOnClickListener
            }
            if (binding.expandableLayoutOrders.visibility == View.VISIBLE) {
                binding.expandableLayoutOrders.visibility = View.GONE
                binding.btnOpenOrders.animate().apply {
                    duration = 500
                    rotationX(-0f)
                }.start()
                return@setOnClickListener
            }
        }
    }

    private fun userObserver() {
        viewModel.user.observe(viewLifecycleOwner) { user ->

            binding.tvProfileName.text = user.username

            binding.tvProfileGender.let { gender ->
                if (user.gender == "Male") {
                    gender.text = user.gender
                    gender.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.secondaryColor
                        )
                    )
                    binding.profileImageView.setBackgroundResource(R.drawable.blue_circle)
                    binding.profileImageView.setImageResource(R.drawable.ic_person)
                }
                if (user.gender == "Female") {
                    gender.text = user.gender
                    gender.setTextColor(ContextCompat.getColor(requireContext(), R.color.pink))
                    binding.profileImageView.setBackgroundResource(R.drawable.pink_circle)
                    binding.profileImageView.setImageResource(R.drawable.ic_person_woman)
                }
            }

            binding.buttonProfileEditProfile.setOnClickListener {

                val dialog =
                    LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
                val builder = AlertDialog.Builder(context).setView(dialog).create()
                builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                builder.setCancelable(true)

                val etName = dialog.findViewById<TextInputLayout>(R.id.etEditName)
                etName.editText?.setText(user.username)
                val etNumber = dialog.findViewById<TextInputLayout>(R.id.etEditNumber)
                etNumber.editText?.setText(user.number)

                dialog.findViewById<ImageView>(R.id.btnCloseDialog).setOnClickListener {
                    builder.dismiss()
                }

                dialog.findViewById<Button>(R.id.btnEditProfile).setOnClickListener {
                    if (etName.editText?.text.toString() != "" && etNumber.editText?.text.toString() != "") {
                        val updatedUser = hashMapOf<String, Any>(
                            "email" to user.email,
                            "username" to etName.editText?.text.toString(),
                            "password" to user.password,
                            "number" to etNumber.editText?.text.toString(),
                            "gender" to user.gender
                        )
                        viewModel.updateUser(updatedUser)
                        builder.dismiss()
                        Toast.makeText(
                            requireContext(), "Updated!!!", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (etName.editText?.text.toString() == "") {
                            etName.error = "Please Enter Your Name"
                        }
                        if (etNumber.editText?.text.toString() == "") {
                            etNumber.error = "Please Enter Your Number"
                        }
                        Toast.makeText(
                            requireContext(),
                            "Please Fill Blanks",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                builder.show()
            }
        }
    }

    private fun orderObserver() {
        viewModel.orderList.observe(viewLifecycleOwner) { orderList ->
            with(binding) {
                if (orderList.isNullOrEmpty()) {
                    cvOrderNotFound.visibility = View.VISIBLE
                    rvOrders.visibility = View.GONE
                } else {
                    cvOrderNotFound.visibility = View.GONE
                    rvOrders.visibility = View.VISIBLE
                    adapterHistory = ProductAdapter(
                        orderList as MutableList<Product>,
                        requireContext(),
                        layoutInflater,
                        favoritesViewModel
                    )
                    rvOrderHistory = rvOrders
                    rvOrderHistory.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvOrderHistory.adapter = adapterHistory
                }

                if (orderList.isNullOrEmpty()) {
                    cvNoCurrentOrder.visibility = View.VISIBLE
                    cvCurrentOrder.visibility = View.GONE
                }else{
                    cvNoCurrentOrder.visibility = View.GONE
                    cvCurrentOrder.visibility = View.VISIBLE
                    adapterCurrent = ProductAdapter(
                        orderList as MutableList<Product>,
                        requireContext(),
                        layoutInflater,
                        favoritesViewModel
                    )
                    rvOrderCurrent = rvCurrentOrders
                    rvOrderCurrent.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    rvOrderCurrent.adapter = adapterCurrent
                }
            }
        }
    }
}