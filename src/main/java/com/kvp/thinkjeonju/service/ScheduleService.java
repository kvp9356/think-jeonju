package com.kvp.thinkjeonju.service;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MoneyDTO;
import com.kvp.thinkjeonju.dto.ScheSpotDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.dto.ScheduleDTO;
import com.kvp.thinkjeonju.model.Money;
import com.kvp.thinkjeonju.model.Schedule;
import com.kvp.thinkjeonju.repository.*;
import com.kvp.thinkjeonju.support.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private ScheSpotMapper scheSpotMapper;

    @Autowired
    private SpotMapper spotMapper;

    @Autowired
    private MoneyMapper moneyMapper;

    @Autowired
    private LikeToMapper likeToMapper;


    public List<ScheduleDTO> getBestSchedules() {
        return scheduleMapper.findBestSchedules().stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(scheduleMapper.getLikeCnt(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }

    public int setScheduleLike(LikeToDTO likeDTO) {
        likeToMapper.setLike(likeDTO);
        return likeToMapper.getLikeCountByScheduleId(likeDTO.getRelatedId());
    }

    public int cancelScheduleLike(LikeToDTO likeDTO) {
        likeToMapper.cancelLike(likeDTO);
        return likeToMapper.getLikeCountByScheduleId(likeDTO.getRelatedId());
    }

    public ScheduleDTO getScheduleById(String scheduleId){
        return scheduleMapper.getScheduleById(scheduleId).toDTO();
    }

    public List<MoneyDTO> getMoneyById(String scheduleId){
        return moneyMapper.getMoneyById(scheduleId).stream()
                .map(Money -> Money.toDTO())
                .collect(Collectors.toList());
    }

    public List<MoneyDTO> getSpotMoney(String id, String scheDate, String scheSpotId){
        return moneyMapper.getSpotMoney(new MoneyDTO(id,scheSpotId,null,0,LocalDate.parse(scheDate))).stream()
                .map(Money -> Money.toDTO())
                .collect(Collectors.toList());
    }

    public List<ScheSpotDTO> getScheSpotById(String scheduleId){
        return scheSpotMapper.getScheSpotById(scheduleId).stream()
                .map(scheSpot -> scheSpot.toDTO())
                .map(scheSpotDTO ->{
                    scheSpotDTO.setSpotimg(spotMapper.findSpotImgUrlById(scheSpotDTO.getSpotId()).get(0));
                    scheSpotDTO.setLikeCnt(spotMapper.getLikeCnt(scheSpotDTO.getSpotId()));
                    return scheSpotDTO;
                })
                .collect(Collectors.toList());
    }


    public void addSchedule(ScheduleDTO scheduleDTO){
        scheduleMapper.addSchedule(scheduleDTO);
    }

    public void updateSchedule(ScheduleDTO scheduleDTO){
        scheduleMapper.updateSchedule(scheduleDTO);
    }

    public void changeWriting(String scheduleId){
        scheduleMapper.changeWriting(scheduleId);
    }

    public int isExistSchedule(String id){
        return scheduleMapper.isExistSchedule(id);
    }

    public void insertScheSpot(List<ScheSpotDTO> scheSpot){
        for(int i =0; i<scheSpot.size();i++){
            scheSpotMapper.insertScheSpot(scheSpot.get(i));
        }
    }

    public void deleteScheSpot(String id){
        scheSpotMapper.deleteScheSpot(id);
    }

    public void insertMoney(List<MoneyDTO> money){
        for(int i =0; i<money.size();i++){
            moneyMapper.insertMoney(money.get(i));
        }
    }

    public void deleteMoney(String id){
        moneyMapper.deleteMoney(id);
    }


    public List<ScheduleDTO> getSchedulesByPageNo(Paging paging) {
        return scheduleMapper.findSchedulesByPage(paging).stream()
                .map(schedule -> schedule.toDTO())
                .map(scheduleDTO -> {
                    scheduleDTO.setLike(likeToMapper.getLikeCountByScheduleId(scheduleDTO.getId()));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }

    public Paging getPaging(int page) {
        Paging paging = new Paging();
        paging.makeLastPage(scheduleMapper.getSchedulesCount());
        paging.makeBlock(page);
        paging.makeCurrentBlock(page);
        return paging;
    }

    public List<ScheduleDTO> setLike(MemberDTO user, List<ScheduleDTO> schedules) {
        return schedules.stream()
                .map(scheduleDTO -> {
                    scheduleDTO.setIsLike(likeToMapper.isLikeScheduleByMemberId(new LikeToDTO(user.getId(), scheduleDTO.getId(), 'c')));
                    return scheduleDTO;
                })
                .collect(Collectors.toList());
    }
}
