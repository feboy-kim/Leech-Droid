package top.okeep.leech.ui.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import top.okeep.leech.SubjectsActivity
import top.okeep.leech.databinding.SubjectsFragmentBinding

class SubjectsFragment : Fragment() {
    private val _vm: SubjectsViewModel by viewModels()
    private val _dm: SubjectsActivity.DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = SubjectsFragmentBinding.inflate(inflater, container, false)
        _vm.entities.observe(viewLifecycleOwner, Observer { list ->
            binding.recycler.adapter = SubjectAdapter(list)
        })
        binding.broker = Broker()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (_dm.dataChanged) {
            _vm.loadSubjects(_dm.db.subjectAccessor())
            _dm.dataChanged = false
        }
        if (_vm.entities.value == null) {                    // Keep from loading when rotating
            _vm.loadSubjects(_dm.db.subjectAccessor())
        }
    }

    inner class Broker {
        fun newSubject(v: View) {
            val action = SubjectsFragmentDirections.actionSubjectsFragmentToSubjectEditFragment()
            v.findNavController().navigate(action)
        }
    }
}