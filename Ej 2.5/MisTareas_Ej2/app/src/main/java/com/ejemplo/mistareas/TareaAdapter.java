package com.ejemplo.mistareas;

import android.content.Context;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ejemplo.mistareas.databinding.ItemTareaBinding;
import com.google.android.material.color.MaterialColors;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    /** Eventos que el adaptador avisa a la Activity. */
    public interface OnTareaListener {
        void onTareaPulsada(int posicion);
        void onMenuTarea(View ancla, int posicion);
        void onCrearMenuContextual(ContextMenu menu, int posicion);
    }

    private final List<Tarea> tareas;
    private final OnTareaListener listener;

    public TareaAdapter(List<Tarea> tareas, OnTareaListener listener) {
        this.tareas = tareas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TareaViewHolder(ItemTareaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        holder.mostrar(tareas.get(position));
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    class TareaViewHolder extends RecyclerView.ViewHolder {
        private final ItemTareaBinding binding;
        private final Context ctx;

        TareaViewHolder(ItemTareaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.ctx = binding.getRoot().getContext();

            // Pulsación corta: marcar / desmarcar como completada
            binding.getRoot().setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onTareaPulsada(pos);
            });
            // Botón de los tres puntos: menú popup de prioridad
            binding.btnMenu.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onMenuTarea(v, pos);
            });
            // Pulsación larga: menú contextual (duplicar / eliminar)
            binding.getRoot().setOnCreateContextMenuListener((menu, v, info) -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onCrearMenuContextual(menu, pos);
            });
        }

        void mostrar(Tarea tarea) {
            binding.tvTitulo.setText(tarea.getTitulo());

            String desc = tarea.getDescripcion();
            if (desc == null || desc.isEmpty()) {
                binding.tvDescripcion.setVisibility(View.GONE);
            } else {
                binding.tvDescripcion.setText(desc);
                binding.tvDescripcion.setVisibility(View.VISIBLE);
            }

            // Prioridad con texto y color
            int texto;
            int atributoColor;
            switch (tarea.getPrioridad()) {
                case Tarea.PRIORIDAD_ALTA:
                    texto = R.string.prioridad_alta;
                    atributoColor = com.google.android.material.R.attr.colorError;
                    break;
                case Tarea.PRIORIDAD_BAJA:
                    texto = R.string.prioridad_baja;
                    atributoColor = com.google.android.material.R.attr.colorOutline;
                    break;
                default:
                    texto = R.string.prioridad_media;
                    atributoColor = com.google.android.material.R.attr.colorPrimary;
            }
            binding.tvPrioridad.setText(texto);
            binding.tvPrioridad.setTextColor(MaterialColors.getColor(binding.getRoot(), atributoColor));

            // Completada: título tachado y tarjeta atenuada (siempre se pone el valor contrario
            // en el "else" por el reciclaje de vistas)
            if (tarea.isCompletada()) {
                binding.tvTitulo.setPaintFlags(binding.tvTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.getRoot().setAlpha(0.55f);
            } else {
                binding.tvTitulo.setPaintFlags(binding.tvTitulo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                binding.getRoot().setAlpha(1f);
            }

            // NUEVO: mostrar la fecha límite solo si existe
            if (tarea.getFechaLimite() != null) {
                binding.tvFecha.setText(ctx.getString(R.string.fecha_limite, tarea.getFechaLimite()));
                binding.tvFecha.setVisibility(View.VISIBLE);
            } else {
                binding.tvFecha.setVisibility(View.GONE);
            }
        }
    }
}
