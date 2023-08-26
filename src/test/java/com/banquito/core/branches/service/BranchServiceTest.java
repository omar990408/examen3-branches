package com.banquito.core.branches.service;

import com.banquito.core.branches.exception.CRUDException;
import com.banquito.core.branches.model.Branch;
import com.banquito.core.branches.repository.BranchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @InjectMocks
    private BranchService underTest;

    @Mock
    private BranchRepository branchRepository;


    @Test
    void lookByIdTest() throws CRUDException {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        branch.setId(id);
        branch.setCode("001");
        branch.setName("Branch 1");
        given(branchRepository.findById(id)).willReturn(Optional.of(branch));
        //when
        Branch branchResult = underTest.lookById(id);
        //then
        assertEquals(branch, branchResult);
    }

    @Test
    void lookByIdTestThrowException() throws CRUDException {
        //given
        String id = UUID.randomUUID().toString();
        ;
        given(branchRepository.findById(id)).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.lookById(id))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Branch with id: {" + id + "} does not exist");
    }

    @Test
    void lookByCodeTest() {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        String code = "001";
        branch.setId(id);
        branch.setCode(code);
        branch.setName("Branch 1");
        given(branchRepository.findByCode(code)).willReturn(branch);
        //when
        Branch branchResult = underTest.lookByCode(code);
        //then
        assertEquals(branch, branchResult);
    }

    @Test
    void getAllTest() {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        branch.setId(id);
        branch.setCode("001");
        branch.setName("Branch 1");
        Branch branch2 = new Branch();
        String id2 = UUID.randomUUID().toString();
        branch.setId(id2);
        branch.setCode("002");
        branch.setName("Branch 2");
        given(branchRepository.findAll()).willReturn(List.of(branch, branch2));
        //when
        List<Branch> branches = underTest.getAll();
        //then
        assertEquals(2, branches.size());
    }

    @Test
    void createBranchTest() throws CRUDException {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        branch.setId(id);
        branch.setCode("001");
        branch.setName("Branch 1");
        //when
        underTest.create(branch);
        //then
        ArgumentCaptor<Branch> branchArgumentCaptor = ArgumentCaptor.forClass(Branch.class);
        verify(branchRepository).save(branchArgumentCaptor.capture());
        Branch branchResult = branchArgumentCaptor.getValue();
        assertEquals(branch, branchResult);
    }

    @Test
    void createBranchTestThrowException() throws CRUDException {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        branch.setId(id);
        branch.setCode("001");
        branch.setName("Branch 1");
        given(branchRepository.save(branch)).willThrow(new RuntimeException("Error in branch creation"));
        //when
        //then
        assertThatThrownBy(() -> underTest.create(branch))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Branch cannot be created, error:Error in branch creation");
    }

    @Test
    void updateBranchTest() throws CRUDException {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        branch.setId(id);
        branch.setCode("001");
        branch.setName("Branch 1");
        Branch branchUpdated = new Branch();
        branchUpdated.setName("Branch 1 Updated");
        given(branchRepository.findByCode(branch.getCode())).willReturn(branch);
        //when
        underTest.update(branch.getCode(), branchUpdated);
        //then
        ArgumentCaptor<Branch> branchArgumentCaptor = ArgumentCaptor.forClass(Branch.class);
        verify(branchRepository).save(branchArgumentCaptor.capture());
        Branch branchResult = branchArgumentCaptor.getValue();
        assertEquals(branchUpdated.getName(), branchResult.getName());
    }
    @Test
    void updateBranchTestThrowExceptionWhenCodeNoExist() throws CRUDException {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        String code = "001";
        given(branchRepository.findByCode(code)).willReturn(null);
        //when
        //then
        assertThatThrownBy(() -> underTest.update(code, branch))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Branch with code: {"+code+"} does not exist");
    }

    @Test
    void updateBranchTestThrowException() throws CRUDException {
        //given
        Branch branch = new Branch();
        String id = UUID.randomUUID().toString();
        String code = "001";
        given(branchRepository.findByCode(code)).willThrow(new RuntimeException("Error in branch update"));
        //when
        //then
        assertThatThrownBy(() -> underTest.update(code, branch))
                .isInstanceOf(CRUDException.class)
                .hasMessageContaining("Branch cannot be updated, error:Error in branch update");
    }
}