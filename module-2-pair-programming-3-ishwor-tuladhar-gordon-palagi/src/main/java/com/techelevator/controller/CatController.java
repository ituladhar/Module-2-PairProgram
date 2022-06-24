package com.techelevator.controller;

import com.techelevator.dao.CatCardDao;
import com.techelevator.model.CatCard;
import com.techelevator.model.CatCardNotFoundException;
import com.techelevator.model.CatFact;
import com.techelevator.model.CatPic;
import com.techelevator.services.CatFactService;
import com.techelevator.services.CatPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/cards")
public class CatController {

    private CatCardDao catCardDao;
    private CatFactService catFactService;
    private CatPicService catPicService;

    @Autowired
    public CatController(CatCardDao catCardDao, CatFactService catFactService, CatPicService catPicService) {
        this.catCardDao = catCardDao;
        this.catFactService = catFactService;
        this.catPicService = catPicService;
    }
    //Hint: Consider starting with the controller method that provides a new, randomly created card.
    //Provides a new, randomly created Cat Card containing information from the cat fact and picture services.

    @GetMapping("/random")
    public CatCard getRandom(){

        CatCard catCard = new CatCard();

        //API Service to get a random cat fact.
        CatFact fact = catFactService.getFact();
        String catFact = fact.getText();
        catCard.setCatFact(catFact);

        //API Service to get a random cat picture.
        CatPic image= catPicService.getPic();
        String imgUrl = image.getFile();
        catCard.setImgUrl(imgUrl);

        return  catCard;
    }

    //Provides a list of all Cat Cards in the user's collection.
    @GetMapping()
    public List<CatCard> getCatCardAll(){
      return catCardDao.list();
    }

    //Provides a Cat Card with the given ID.
    @GetMapping("/{id}")
    public CatCard getCatCardById(@PathVariable long id){
        return catCardDao.get(id);
    }

    //Saves a card to the user's collection.
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public boolean saveCatCard(@Valid @RequestBody CatCard catCard){
        return catCardDao.save(catCard);
    }

    //Updates a card in the user's collection.
    @PutMapping("/{id}")
    public boolean updateCatCard(@Valid @PathVariable long id,
                                 @RequestBody CatCard catCard
                                ) throws CatCardNotFoundException {
        return catCardDao.update(id, catCard);
    }

    //Removes a card from the user's collection.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public boolean deleteCatCardById(@PathVariable long id) throws CatCardNotFoundException{
        return catCardDao.delete(id);
    }
}
