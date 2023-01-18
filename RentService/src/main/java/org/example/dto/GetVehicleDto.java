package org.example.dto;

public class GetVehicleDto {
    //Dto koji vraca vozilo kao rezultat neke pretrage
    // (npr kada korisnik trazi slobodna vozila za neki termin vrati se lista ovih Dto-ova)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
