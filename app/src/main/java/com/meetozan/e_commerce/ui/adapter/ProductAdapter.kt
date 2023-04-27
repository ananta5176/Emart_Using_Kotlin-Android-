package com.meetozan.e_commerce.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.meetozan.e_commerce.R
import com.meetozan.e_commerce.data.model.model.Product
import com.meetozan.e_commerce.databinding.ProductCardBinding
import com.meetozan.e_commerce.ui.favorites.FavoritesViewModel
import com.squareup.picasso.Picasso

class ProductAdapter(
    private val productList: List<Product>,
    private val context: Context,
    private val layoutInflater: LayoutInflater,
    private val favoritesViewModel: FavoritesViewModel
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(private var binding: ProductCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            with(binding) {
                productCard = product

                Picasso.get().load(product.picUrl)
                    .centerCrop()
                    .resize(1000, 1000)
                    .into(productCardImage)

                cvProduct.setOnClickListener {
                    val imageList = ArrayList<SlideModel>()
                    val dialog = BottomSheetDialog(context)
                    val view = layoutInflater.inflate(R.layout.bottom_sheet_detail, null)

                    imageList.add(SlideModel(product.picUrl))
                    imageList.add(SlideModel(product.secondPicUrl))
                    imageList.add(SlideModel(product.thirdPicUrl))

                    dialog.setContentView(view)

                    view.findViewById<TextView>(R.id.tvDetailName).text = product.productName
                    view.findViewById<TextView>(R.id.tvDetailPrice).text = product.price.toString()
                    view.findViewById<TextView>(R.id.tvDetailBrand).text = product.brand
                    view.findViewById<TextView>(R.id.tvDetailDescription).text = product.description
                    view.findViewById<ImageSlider>(R.id.imageSliderDetail).setImageList(imageList)
                    val favSwitch = view.findViewById<SwitchMaterial>(R.id.favoriteSwitch)
                    val stock = view.findViewById<TextView>(R.id.tvDetailStock)
                    stock.text = product.stock.toString()

                    if (this.productCard?.stock!! <= 10) {
                        stock.visibility = View.VISIBLE
                        view.findViewById<TextView>(R.id.tvStock).visibility = View.VISIBLE
                    }

                    favSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            val productHashMap = hashMapOf<Any, Any>(
                                "id" to product.id,
                                "productName" to product.productName,
                                "price" to product.price,
                                "brand" to product.brand,
                                "picUrl" to product.picUrl,
                                "secondPicUrl" to product.secondPicUrl,
                                "thirdPicUrl" to product.thirdPicUrl,
                                "description" to product.description,
                                "isFavorite" to true,
                                "rate" to product.rate,
                                "stock" to product.stock
                            )

                            buttonView.text = "♥"

                            favoritesViewModel.addToFavorites(product, productHashMap)
                            Toast.makeText(context, "Added to Favs", Toast.LENGTH_SHORT).show()
                        }else{
                            favoritesViewModel.deleteFromFavorites(product)
                            buttonView.text = ""
                            Toast.makeText(context,"Removed from Favs",Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(productList[position])

}