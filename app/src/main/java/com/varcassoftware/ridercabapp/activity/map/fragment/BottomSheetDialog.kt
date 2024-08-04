package com.varcassoftware.ridercabapp.activity.map.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.map.SearchDestinationAdapter
import com.varcassoftware.ridercabapp.databinding.BottomSheetLayoutBinding

class BottomSheetDialog : BottomSheetDialogFragment(),
    SearchDestinationAdapter.OnClickItemListener {

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = _binding!!
    private var listener: OnClickButtonListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        setupRecyclerView()
    }

    private fun setClickListeners() {
        binding.searchDestination.setOnClickListener {
            //listener?.onClickButtonClickedOn("1SD", "hello dear")
            //dismiss()
            fullScreenView()
        }

    }


    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SearchDestinationAdapter() // Replace with your actual adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.findViewById<Button>(R.id.conformLocationButton)?.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnClickButtonListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val bottomSheet =
            dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = it.height
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    Toast.makeText(context, "onSlide", Toast.LENGTH_SHORT).show()
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            Toast.makeText(context, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_EXPANDED -> {
                            Toast.makeText(context, "STATE_EXPANDED", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            Toast.makeText(context, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_DRAGGING -> {
                            Toast.makeText(context, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_SETTLING -> {
                            Toast.makeText(context, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            Toast.makeText(context, "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })
        }

        return dialog
    }

    /* override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
         val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
         val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
         dialog.setContentView(view)

         dialog.setOnShowListener { dialogInterface ->
             val bottomSheetDialog = dialogInterface as BottomSheetDialog
             val bottomSheet =
                 bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
             bottomSheet?.let {
                 val behavior = BottomSheetBehavior.from(it)
                 behavior.peekHeight = it.height
                 behavior.state = BottomSheetBehavior.STATE_EXPANDED
                 behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                     override fun onSlide(bottomSheet: View, slideOffset: Float) {
                         Toast.makeText(context, "onSlide", Toast.LENGTH_SHORT).show()
                     }

                     override fun onStateChanged(bottomSheet: View, newState: Int) {
                         //showing the different states.
                         when (newState) {
                             BottomSheetBehavior.STATE_HIDDEN -> {
                                 Toast.makeText(context, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                             }

                             BottomSheetBehavior.STATE_EXPANDED -> {
                                 Toast.makeText(context, "STATE_EXPANDED", Toast.LENGTH_SHORT).show()
                             }

                             BottomSheetBehavior.STATE_COLLAPSED -> {
                                 Toast.makeText(context, "STATE_COLLAPSED", Toast.LENGTH_SHORT)
                                     .show()
                             }

                             BottomSheetBehavior.STATE_DRAGGING -> {
                                 Toast.makeText(context, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
                             }

                             BottomSheetBehavior.STATE_SETTLING -> {
                                 Toast.makeText(context, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
                             }

                             BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                                 Toast.makeText(context, "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT)
                                     .show()
                             }
                         }
                     }
                 })

             }
             dialog.setOnCancelListener {
                 // Doesn't matter what you write here, the dialog will be closed.
             }
             dialog.setOnDismissListener {
                 // Doesn't matter what you write here, the dialog will be closed.
             }
         }
         return dialog
     }*/

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet = it.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    private fun fullScreenView() {
        dialog?.let {
            val bottomSheet = it.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { bs ->
                val layoutParams = bs.layoutParams
                layoutParams.height =
                    if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    } else {
                        ViewGroup.LayoutParams.MATCH_PARENT

                    }

                if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    binding.rlSearchDesitination.visibility = View.VISIBLE
                    binding.cdDestination.visibility = View.GONE
                } else {
                    binding.rlSearchDesitination.visibility = View.GONE
                    binding.cdDestination.visibility = View.VISIBLE
                }
                bs.layoutParams = layoutParams
                val behavior = BottomSheetBehavior.from(bs)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }


    private fun getScreenHeight(): Int {
        return resources.displayMetrics.heightPixels
    }

    interface OnClickButtonListener {
        fun onClickButtonClickedOn(forWhat: String, massage: String)
    }

    override fun onItemClicked(forWhat: String) {
        dismiss()
        listener?.onClickButtonClickedOn("address", forWhat)
    }
}
