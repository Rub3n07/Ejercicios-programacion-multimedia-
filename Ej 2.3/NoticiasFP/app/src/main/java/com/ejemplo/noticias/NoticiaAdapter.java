package com.ejemplo.noticias;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ejemplo.noticias.databinding.ItemNoticiaBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;

import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {

    public interface OnNoticiaListener {
        void onLeer(Noticia noticia);
        void onFavorita(int posicion);
    }

    private final List<Noticia> noticias;
    private final OnNoticiaListener listener;

    public NoticiaAdapter(List<Noticia> noticias, OnNoticiaListener listener) {
        this.noticias = noticias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticiaViewHolder(ItemNoticiaBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        holder.mostrar(noticias.get(position));
    }

    @Override
    public int getItemCount() {
        return noticias.size();
    }

    class NoticiaViewHolder extends RecyclerView.ViewHolder {
        private final ItemNoticiaBinding binding;

        NoticiaViewHolder(ItemNoticiaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnLeer.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onLeer(noticias.get(pos));
            });
            binding.btnFavorita.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onFavorita(pos);
            });
        }

        void mostrar(Noticia n) {
            binding.tvTitulo.setText(n.getTitulo());
            binding.tvResumen.setText(n.getResumen());

            int texto, icono;
            switch (n.getCategoria()) {
                case Noticia.DEPORTES:
                    texto = R.string.seccion_deportes; icono = R.drawable.ic_deportes; break;
                case Noticia.TECNOLOGIA:
                    texto = R.string.seccion_tecnologia; icono = R.drawable.ic_tecnologia; break;
                default:
                    texto = R.string.seccion_cultura; icono = R.drawable.ic_cultura;
            }
            binding.chipCategoria.setText(texto);
            binding.chipCategoria.setChipIconResource(icono);

            binding.btnFavorita.setImageResource(
                    n.isFavorita() ? R.drawable.ic_star : R.drawable.ic_star_border);

            // AMPLIACIÓN 4: tarjeta "Filled" para favoritas, "Elevated" para el resto.
            // Filled = fondo más marcado y sin sombra ("destacada").
            // Elevated = fondo suave y con sombra ("flota" sobre la pantalla).
            // Se pone SIEMPRE un valor u otro por el reciclaje de vistas.
            MaterialCardView card = binding.getRoot();
            float densidad = card.getResources().getDisplayMetrics().density;
            if (n.isFavorita()) {
                card.setCardBackgroundColor(MaterialColors.getColor(card,
                        com.google.android.material.R.attr.colorSurfaceContainerHighest));
                card.setCardElevation(0f);
            } else {
                card.setCardBackgroundColor(MaterialColors.getColor(card,
                        com.google.android.material.R.attr.colorSurfaceContainerLow));
                card.setCardElevation(1f * densidad);
            }
        }
    }
}
