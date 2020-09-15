package com.example.Julien3DBack.Categorie;

import com.example.Julien3DBack.Image.Image;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name="ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @OneToMany(mappedBy = "categorie")
    private Set<Image> image;

    public Set<Image> getImage() {
        return image;
    }

    public void setImage(Set<Image> image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
