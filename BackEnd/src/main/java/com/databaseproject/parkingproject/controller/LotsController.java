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
@CrossOrigin
public class LotsController {
    private final LotDaoImpl lotDao;

    @GetMapping("/accept/adding/lot")
    public ResponseMessageDto acceptAddingLot(@RequestParam int id) {
        return lotDao.admitLot(id);
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

    @DeleteMapping("/delete/lot")
    public ResponseMessageDto deleteLot(@RequestParam int id) {
        return lotDao.deleteLot(id);
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
    @GetMapping("/nearest/lots")
    public ParkingLots[]  findAndPrintNearestLots(@RequestParam double userLat,@RequestParam double userLng) {
        List<ParkingLots> nearestLots = lotDao.findNearestParkingLots(userLat, userLng, 5);
        nearestLots.forEach(lot -> System.out.println("Lot: " + lot));
        return nearestLots.toArray(new ParkingLots[0]);
    }


}
