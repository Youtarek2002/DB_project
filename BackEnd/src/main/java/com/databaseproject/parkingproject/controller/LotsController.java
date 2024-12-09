package com.databaseproject.parkingproject.controller;

import com.databaseproject.parkingproject.dao.LotDaoImpl;
import com.databaseproject.parkingproject.dto.ResponseMessageDto;
import com.databaseproject.parkingproject.entity.ParkingLots;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authenticate")
@RequiredArgsConstructor
public class LotsController {
    private final LotDaoImpl lotDao;

    @GetMapping("/accept/adding/lot")
    public ResponseMessageDto acceptAddingLot(@RequestParam int id) {
        return lotDao.addmitLot(id);
    }

    @PostMapping("/request/adding/lot")
    public ResponseMessageDto requestAddingLot(@RequestBody ParkingLots parkingLot) {
        System.out.println(parkingLot.toString());
        return lotDao.requsetInsertingLot(parkingLot);
    }

    @GetMapping("/deny/adding/lot/request")
    public ResponseMessageDto denyAddingLot(@RequestParam int id) {
        return lotDao.denyAddingLot(id);
    }
    @GetMapping("/all/pending/lots")
    public ParkingLots[] allPendingApproved() {
        List<ParkingLots> lotList = lotDao.getAllPendingLots();
        return lotList.toArray(new ParkingLots[0]);
    }
    @GetMapping("/manager/lots")
    public ParkingLots[] getManagerLots(@RequestParam int managerId) {
        List<ParkingLots> lotList = lotDao.getManagerLots(managerId);
        return lotList.toArray(new ParkingLots[0]);
    }

}
